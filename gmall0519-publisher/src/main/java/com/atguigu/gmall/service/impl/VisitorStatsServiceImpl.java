package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.beans.VisitorStats;
import com.atguigu.gmall.mapper.VisitorStatsMapper;
import com.atguigu.gmall.service.VisitorStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 访客统计接口实现类
 */
@Service
public class VisitorStatsServiceImpl implements VisitorStatsService {

    @Autowired
    private VisitorStatsMapper visitorStatsMapper;

    @Override
    public List<VisitorStats> getVisitorStatsByIsnew(Integer date) {
        return visitorStatsMapper.selectVisitorStatsByIsnew(date);
    }

    @Override
    public List<VisitorStats> getVisitorStatsByIsHr(Integer date) {
        return visitorStatsMapper.selectVisitorStatsByHr(date);
    }
}
