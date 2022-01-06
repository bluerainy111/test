package com.atguigu.gmall.mapper;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;


public interface ProductStatsMapper {
    @Select("select sum(order_amount) order_amount from product_stats_2021 where toYYYYMMDD(stt)=#{date}")
    BigDecimal selectGMV (Integer date);
}
