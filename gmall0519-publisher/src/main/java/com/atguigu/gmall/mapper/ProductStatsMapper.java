package com.atguigu.gmall.mapper;

import com.atguigu.gmall.beans.ProductStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/16
 * Desc: 商品相关统计Mapper
 */
public interface ProductStatsMapper {
    //获取某一天的总交易额
    @Select("select sum(order_amount) order_amount from product_stats_0519 where toYYYYMMDD(stt)=#{date}")
    BigDecimal selectGMV(Integer date);

    //获取某一天品牌以及对应的交易额
    @Select("select tm_id,tm_name,sum(order_amount) order_amount from product_stats_0519 " +
        " where toYYYYMMDD(stt)=#{date} group by tm_id,tm_name having order_amount > 0 " +
        " order by order_amount desc limit #{limit}")
    List<ProductStats> selectProductStatsByTm(@Param("date") Integer date, @Param("limit") Integer limit);

    //获取某一天类别以及对应的交易额
    @Select("select category3_id,category3_name,sum(order_amount) order_amount from product_stats_0519 " +
        " where toYYYYMMDD(stt)=#{date} group by category3_id,category3_name having order_amount > 0 " +
        " order by order_amount desc limit #{limit}")
    List<ProductStats> selectProductStatsByCategory3(@Param("date") Integer date, @Param("limit") Integer limit);

    //获取某一天SPU以及对应的交易额
    @Select("select spu_id,spu_name,sum(order_amount) order_amount,sum(order_ct) order_ct from product_stats_0519 " +
        " where toYYYYMMDD(stt)=#{date} group by spu_id,spu_name having order_amount > 0  " +
        " order by order_amount desc limit #{limit}")
    List<ProductStats> selectProductStatsBySpu(@Param("date") Integer date, @Param("limit") Integer limit);

}

