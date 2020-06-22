package cn.mingzhu.iot.app.bas.mapper;

import tk.mybatis.mapper.additional.aggregation.AggregationMapper;
import tk.mybatis.mapper.common.Mapper;

@tk.mybatis.mapper.annotation.RegisterMapper
public interface AutoMapper<T> extends Mapper<T>, AggregationMapper<T> {

}