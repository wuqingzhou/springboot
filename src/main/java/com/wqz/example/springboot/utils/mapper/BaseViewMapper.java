package com.wqz.example.springboot.utils.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;

/**
 * 适用于所有表、视图
 *
 * @param <T>
 * @author wuqingzhou
 */
public interface BaseViewMapper<T> extends
        SelectByConditionRowBoundsMapper<T>,
        SelectRowBoundsMapper<T>,
        BaseSelectMapper<T>,
        InsertListMapper<T>,
        ConditionMapper<T> {
}
