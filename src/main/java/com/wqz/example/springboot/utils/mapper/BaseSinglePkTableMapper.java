package com.wqz.example.springboot.utils.mapper;

import tk.mybatis.mapper.common.IdsMapper;

/**
 * 适用于单主键的表
 * 要求有且只有一个主键（即：要求实体类中有且只有一个带有@Id注解的字段）
 *
 * @param <T>
 * @author wuqingzhou
 */
public interface BaseSinglePkTableMapper<T> extends
        IdsMapper<T>,
        BaseCompositeOrNullPkTableMapper<T> {
}
