create or replace procedure push_quality_code(p_dbId             in varchar2,
                                              p_job_section_code in varchar2,
                                              p_table_name       in varchar2,
                                              p_metric           in varchar2,
                                              p_qa_name          in varchar2) is
  cursor allStatic is
    select job_name,
           inoutlet_name,
           table_name,
           process_time,
           metric,
           sum(metric_value) as metric_value
      from tb_dfdi_job_static t
     where has_qa_count = '0'
       and inoutlet_name = 'runlet'
       and job_name = p_job_section_code
       and table_name = p_table_name
       and metric = p_metric
     group by job_name, inoutlet_name, table_name, metric, process_time;
  p_start_time date;
  p_end_time   date;
  p_duration   number;
  p_output_num number;

  p_commit_limit    number;
  p_wait_commit_num number;
begin
  p_start_time := null;
  p_end_time   := null;
  p_duration   := 300;

  p_commit_limit    := 200;
  p_wait_commit_num := 0;

  for cr in allStatic loop
    if cr.process_time is not null then
      p_start_time := to_date(cr.process_time, 'yyyymmddhh24miss');
      p_end_time   := p_start_time + p_duration / (24 * 60 * 60);
    end if;

    -- 计算总量
    select sum(t.METRIC_VALUE)
      into p_output_num
      from tb_dfdi_job_static t
     where 1 = 1
       and t.metric = 'rows-valid'
       and t.inoutlet_name = 'output'
       and job_name = p_job_section_code
       and table_name = p_table_name
       and process_time = cr.process_time;

    if p_output_num < cr.metric_value then
      p_output_num := cr.metric_value;
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
       p_dbId,
       p_table_name,
       null,
       p_metric,
       p_qa_name,
       null,
       p_start_time,
       p_end_time,
       null,
       null,
       null,
       null,
       p_output_num,
       cr.metric_value,
       null,
       null,
       null,
       null,
       null,
       null,
       '0',
       sysdate);

    update tb_dfdi_job_static
       set has_qa_count = '1'
     WHERE 1 = 1
       and has_qa_count = '0'
       and job_name = p_job_section_code
       and table_name = p_table_name
       and metric = p_metric
       and inoutlet_name = 'runlet'
       and process_time = cr.process_time;

    p_wait_commit_num := p_wait_commit_num + 1;
    if p_wait_commit_num >= p_commit_limit then
      commit;
      p_wait_commit_num := 0;
    end if;
  end loop;
  commit;
end push_quality_code;