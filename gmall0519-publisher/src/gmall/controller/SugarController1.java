package com.atguigu.gmall.controller;

import com.atguigu.gmall.mapper.ProductStatsMapper;
import com.atguigu.gmall.service.ProductStatsService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

@RestController
public class SugarController1 {
    @Autowired
    private ProductStatsService productStatsService;
    @RequestMapping("api/sugar/gmv")
    public String getGMV(@RequestParam(value = "date",defaultValue = "0") Integer date){
        //如果没有传递日期,将会传给当天变量
        if (date == 0){
            date=now();
        }
        BigDecimal gmv = productStatsService.getGMV(date);
        String json = "{\"status\": 0,\"data\": " + gmv + "}";
        return json;
    }

    private Integer now() {
    String yyyyMMdd= DateFormatUtils.format(new Date(),"yyyyMMdd");
    return Integer.valueOf(yyyyMMdd);
    }

}
