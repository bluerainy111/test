package com.atguigu.gmall.service;

import com.atguigu.gmall.beans.ProductStats;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/16
 * Desc: 商品统计Service接口
 */
public interface ProductStatsService {
    BigDecimal getGMV(Integer date);

    List<ProductStats> getProductStatsByTm(Integer date,Integer limit);

    List<ProductStats> getProductStatsByCategory3(Integer date,Integer limit);

    List<ProductStats> getProductStatsBySpu(Integer date,Integer limit);
}
