package com.atguigu.gmall.service;

import com.atguigu.gmall.beans.KeywordStats;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 关键词统计Service接口
 */
public interface KeywordStatsService {

    List<KeywordStats> getKeywordStats(Integer date,Integer limit);

}
