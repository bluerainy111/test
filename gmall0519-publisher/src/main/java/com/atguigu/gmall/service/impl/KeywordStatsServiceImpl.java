package com.atguigu.gmall.service.impl;

import com.atguigu.gmall.beans.KeywordStats;
import com.atguigu.gmall.mapper.KeywordStatsMapper;
import com.atguigu.gmall.service.KeywordStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 关键词统计接口实现类
 */
@Service
public class KeywordStatsServiceImpl implements KeywordStatsService {
    @Autowired
    private KeywordStatsMapper keywordStatsMapper;
    @Override
    public List<KeywordStats> getKeywordStats(Integer date, Integer limit) {
        return keywordStatsMapper.selectKeywordStats(date,limit);
    }
}
