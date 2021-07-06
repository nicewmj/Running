package com.example.mapper;

import tk.mybatis.mapper.additional.dialect.oracle.InsertListMapper;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义BaseMapper让子接口继承(我们一般直接继承Mapper<T>，但那个不够强大)
 * <p>
 * Mapper接口：基本的增、删、改、查方法
 * IdListMapper：支持根据IdList批量查询和删除
 * InsertListMapper：支持批量插入
 * <p>
 * 注意：一定要加@RegisterMapper，注册接口
 *
 * @author bravo
 * @date 2020-01-22 21:00
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T, Long>, InsertListMapper<T> {
}