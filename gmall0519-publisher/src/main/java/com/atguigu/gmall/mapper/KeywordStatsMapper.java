package com.atguigu.gmall.mapper;

import com.atguigu.gmall.beans.KeywordStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 关键词统计Mapper接口
 */
public interface KeywordStatsMapper {
    @Select("select keyword,   " +
        "      sum( " +
        "        keyword_stats_0519.ct *  " +
        "        multiIf( " +
        "          source='SEARCH',10, " +
        "          source='ORDER',5, " +
        "          source='CART',2, " +
        "          source='CLICK',1,0 " +
        "        ) " +
        "      ) ct   " +
        "    from  " +
        "      keyword_stats_0519 " +
        "    where  " +
        "      toYYYYMMDD(stt)=#{date} " +
        "    group by  keyword order by  ct desc  limit #{limit}")
    List<KeywordStats> selectKeywordStats(@Param("date") Integer date, @Param("limit") Integer limit);
}
