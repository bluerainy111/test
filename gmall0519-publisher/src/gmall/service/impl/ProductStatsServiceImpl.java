package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.mapper.ProductStatsMapper;
import com.atguigu.gmall.service.ProductStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class ProductStatsServiceImpl implements ProductStatsService {
    @Autowired
    private ProductStatsMapper productStatsMapper;

    @Override
    public BigDecimal getGMV(Integer date) {
        return productStatsMapper.selectGMV(date);
    }
}
