package com.wqz.example.springboot.utils.mapper;

import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;

/**
 * 适用于复合主键或没有主键的表
 * 要求有主键（一个或多个都行）
 *
 * @param <T>
 * @author wuqingzhou
 */
public interface BaseCompositeOrNullPkTableMapper<T> extends
        BaseDeleteMapper<T>,
        BaseUpdateMapper<T>,
        BaseInsertMapper<T>,
        BaseViewMapper<T> {
}
