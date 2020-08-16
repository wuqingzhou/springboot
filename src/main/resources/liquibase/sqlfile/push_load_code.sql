create or replace procedure push_load_code(dbId             in varchar2,
                                           job_section_code in varchar2,
                                           table_name       in varchar2) is

  -- ????stastic????????
  cursor allStatic is
    select id,
           job_name,
           inoutlet_name,
           table_name,
           process_time,
           process_time_duration,
           ingestion_time,
           ingestion_time_duration,
           event_time,
           event_time_duration,
           batch,
           metric,
           metric_value,
           create_date,
           has_count,
           has_qa_count
      from tb_dfdi_job_static
     where job_name = job_section_code
       and table_name = table_name
       and has_count = '0'
       and metric = 'rows-valid'
       and inoutlet_name = 'output';

  p_start_time date; -- ??????????????
  p_end_time   date; -- ??????????????
  p_duration   number;

  i_start_time date; -- ??????????????
  i_end_time   date; -- ??????????????
  i_duration   number;

  e_start_time date; -- ??????????????
  e_end_time   date; -- ??????????????
  e_duration   number;

  p_commit_limit    number;
  p_wait_commit_num number;
begin
  p_commit_limit    := 200;
  p_wait_commit_num := 0;

  p_start_time := null;
  p_end_time   := null;
  p_duration   := 0;

  i_start_time := null;
  i_end_time   := null;
  i_duration   := 0;

  e_start_time := null;
  e_end_time   := null;
  e_duration   := 0;

  for cr in allStatic loop
    if cr.process_time is not null then
      p_start_time := to_date(cr.process_time, 'yyyy-mm-dd hh24:mi:ss');
      if cr.process_time_duration is not null then
        p_duration := to_number(cr.process_time_duration);
        p_end_time := p_start_time + p_duration / (24 * 60 * 60);
      end if;
    end if;

    if cr.ingestion_time is not null then
      i_start_time := to_date(cr.ingestion_time, 'yyyy-mm-dd hh24:mi:ss');
      if cr.ingestion_time_duration is not null then
        i_duration := to_number(cr.ingestion_time_duration);
        i_end_time := i_start_time + i_duration / (24 * 60 * 60);
      end if;
    end if;

    if cr.event_time is not null then
      e_start_time := to_date(cr.event_time, 'yyyy-mm-dd hh24:mi:ss');
      if cr.event_time_duration is not null then
        e_duration := to_number(cr.event_time_duration);
        e_end_time := e_start_time + e_duration / (24 * 60 * 60);
      end if;
    end if;

    insert into tb_dqc_quality_result
      (id,
       res_id,
       res_db_id,
       res_table_name,
       res_code,
       qa_code,
       qa_name,
       batch,
       pt_start,
       pt_end,
       it_start,
       it_end,
       et_start,
       et_end,
       metric_value1,
       metric_value2,
       metric_value3,
       metric_value4,
       metric_value5,
       metric_value6,
       metric_area,
       metric_spcode,
       flag,
       create_time)
    values
      (SYS_GUID(),
       null,
       dbId,
       table_name,
       null,
       'LOAD',
       '入库增量',
       cr.batch,
       p_start_time,
       p_end_time,
       i_start_time,
       i_end_time,
       e_start_time,
       e_end_time,
       cr.metric_value,
       null,
       null,
       null,
       null,
       null,
       null,
       null,
       '0',
       sysdate);

    update tb_dfdi_job_static t
       set t.has_count = '1'
     WHERE 1 = 1
       and t.id = cr.id;

    p_wait_commit_num := p_wait_commit_num + 1;
    if p_wait_commit_num >= p_commit_limit then
      commit;
      p_wait_commit_num := 0;
    end if;
  end loop;
  commit;
end push_load_code;