package com.wqz.example.springboot.mapper;

import com.wqz.example.springboot.model.TbUser;
import com.wqz.example.springboot.utils.mapper.BaseSinglePkTableMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbUserMapper extends BaseSinglePkTableMapper<TbUser> {

    List<TbUser> getListTest();
    List<Map<String, String>> getListTest2();
}