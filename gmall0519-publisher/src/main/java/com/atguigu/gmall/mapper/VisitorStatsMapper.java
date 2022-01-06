package com.atguigu.gmall.mapper;

import com.atguigu.gmall.beans.VisitorStats;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: Felix
 * Date: 2021/11/17
 * Desc: 访客统计Mapper接口
 */
public interface VisitorStatsMapper {
    //获取某一天新老访客流量
    @Select("select is_new,sum(uv_ct) uv_ct,sum(pv_ct) pv_ct,sum(uj_ct) uj_ct,sum(dur_sum) dur_sum,sum(sv_ct) sv_ct " +
        " from visitor_stats_0519 where toYYYYMMDD(stt)=#{date} group by is_new")
    List<VisitorStats> selectVisitorStatsByIsnew(Integer date);

    //获取某一天每一个小时访问流量
    @Select("select " +
        "   toHour(stt) hr," +
        "   sum(visitor_stats_0519.uv_ct) uv_ct," +
        "   sum(pv_ct) pv_ct," +
        "   sum(if(is_new='1',visitor_stats_0519.uv_ct,0)) new_uv" +
        "  from" +
        "   visitor_stats_0519" +
        "  where toYYYYMMDD(stt)=#{date} group by hr")
    List<VisitorStats> selectVisitorStatsByHr(Integer date);
}
