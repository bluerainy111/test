package com.atguigu.gmall.service;

import com.atguigu.gmall.beans.VisitorStats;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 访客统计service接口
 */
public interface VisitorStatsService {
    List<VisitorStats> getVisitorStatsByIsnew(Integer date);

    List<VisitorStats> getVisitorStatsByIsHr(Integer date);
}
