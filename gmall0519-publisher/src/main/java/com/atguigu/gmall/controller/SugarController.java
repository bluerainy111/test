package com.atguigu.gmall.controller;

import com.atguigu.gmall.beans.KeywordStats;
import com.atguigu.gmall.beans.ProductStats;
import com.atguigu.gmall.beans.ProvinceStats;
import com.atguigu.gmall.beans.VisitorStats;
import com.atguigu.gmall.service.KeywordStatsService;
import com.atguigu.gmall.service.ProductStatsService;
import com.atguigu.gmall.service.ProvinceStatsService;
import com.atguigu.gmall.service.VisitorStatsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * Author: Felix
 * Date: 2021/11/16
 * Desc: 大屏展示控制类
 */
@RestController
@RequestMapping("/api/sugar")
public class SugarController {

    @Autowired
    private ProductStatsService productStatsService;

    @Autowired
    private ProvinceStatsService provinceStatsService;

    @Autowired
    private VisitorStatsService visitorStatsService;

    @Autowired
    private KeywordStatsService keywordStatsService;

    @RequestMapping("/keyword")
    public String getKeywordStats(@RequestParam(value = "date", defaultValue = "0") Integer date,
                                  @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        if (date == 0) {
            date = now();
        }
        List<KeywordStats> keywordStatsList = keywordStatsService.getKeywordStats(date, limit);
        StringBuilder jsonB = new StringBuilder("{\"status\": 0,\"data\": [");
        for (int i = 0; i < keywordStatsList.size(); i++) {
            KeywordStats keywordStats = keywordStatsList.get(i);
            jsonB.append("{\"name\": \"" + keywordStats.getKeyword() + "\",\"value\": " + keywordStats.getCt() + "}");
            if(i < keywordStatsList.size() - 1){
                jsonB.append(",");
            }
        }
        jsonB.append("]}");
        return jsonB.toString();
    }

    @RequestMapping("/hr")
    public String getVisitorStatsByHr(@RequestParam(value = "date", defaultValue = "0") Integer date) {
        if (date == 0) {
            date = now();
        }
        //调用service层的方法获取分时统计的数据
        List<VisitorStats> visitorStatsList = visitorStatsService.getVisitorStatsByIsHr(date);
        //定义一个数组 ，数组的长度是24，数组中的数据类型是VisitorStats
        VisitorStats[] visitorStatArr = new VisitorStats[24];
        //给数组中的元素赋值
        for (VisitorStats visitorStats : visitorStatsList) {
            visitorStatArr[visitorStats.getHr()] = visitorStats;
        }

        //定义4个集合  用于json字符串的拼接
        List hrList = new ArrayList();
        List uvList = new ArrayList();
        List pvList = new ArrayList();
        List newUvList = new ArrayList();

        //对数组进行遍历
        for (int hr = 0; hr < visitorStatArr.length; hr++) {
            VisitorStats visitorStats = visitorStatArr[hr];
            if (visitorStats != null) {
                //说明这个小时有访问数据,将访问数据放到对应的list集合中
                uvList.add(visitorStats.getUv_ct());
                pvList.add(visitorStats.getPv_ct());
                newUvList.add(visitorStats.getNew_uv());
            } else {
                //说明这个小时没有访问数据
                uvList.add(0L);
                pvList.add(0L);
                newUvList.add(0L);
            }
            hrList.add(String.format("%02d", hr));
        }

        String json = "{\"status\":0,\"data\":" +
            "{\"categories\":[\"" + StringUtils.join(hrList, "\",\"") + "\"]," +
            "\"series\":[" +
            "{\"name\":\"PV\",\"data\":[" + StringUtils.join(pvList, ",") + "]}," +
            "{\"name\":\"UV\",\"data\":[" + StringUtils.join(uvList, ",") + "]}," +
            "{\"name\":\"new_UV\",\"data\":[" + StringUtils.join(newUvList, ",") + "]}]}}";

        return json;
    }

    @RequestMapping("/is_new")
    public String getVisitorStatsByIsnew(@RequestParam(value = "date", defaultValue = "0") Integer date) {
        if (date == 0) {
            date = now();
        }
        //调用service方法获取新老访客流量对比数据
        List<VisitorStats> visitorStatsList = visitorStatsService.getVisitorStatsByIsnew(date);
        //定义两个VisitorStats对象，用于接收新老访客信息
        VisitorStats newVisitorStats = new VisitorStats();
        VisitorStats oldVisitorStats = new VisitorStats();
        for (VisitorStats visitorStats : visitorStatsList) {
            if ("1".equals(visitorStats.getIs_new())) {
                newVisitorStats = visitorStats;
            } else {
                oldVisitorStats = visitorStats;
            }
        }

        String json = "{\"status\":0,\"data\":{\"combineNum\":1,\"columns\":" +
            "[{\"name\":\"类别\",\"id\":\"type\"}," +
            "{\"name\":\"新用户\",\"id\":\"new\"}," +
            "{\"name\":\"老用户\",\"id\":\"old\"}]," +
            "\"rows\":" +
            "[{\"type\":\"用户数(人)\"," +
            "\"new\": " + newVisitorStats.getUv_ct() + "," +
            "\"old\":" + oldVisitorStats.getUv_ct() + "}," +
            "{\"type\":\"总访问页面(次)\"," +
            "\"new\":" + newVisitorStats.getPv_ct() + "," +
            "\"old\":" + oldVisitorStats.getPv_ct() + "}," +
            "{\"type\":\"跳出率(%)\"," +
            "\"new\":" + newVisitorStats.getUjRate() + "," +
            "\"old\":" + oldVisitorStats.getUjRate() + "}," +
            "{\"type\":\"平均在线时长(秒)\"," +
            "\"new\":" + newVisitorStats.getDurPerSv() + "," +
            "\"old\":" + oldVisitorStats.getDurPerSv() + "}," +
            "{\"type\":\"平均访问页面数(人次)\"," +
            "\"new\":" + newVisitorStats.getPvPerSv() + "," +
            "\"old\":" + oldVisitorStats.getPvPerSv()
            + "}]}}";
        return json;

    }

    @RequestMapping("/province")
    public String getProvinceStats(
        @RequestParam(value = "date", defaultValue = "0") Integer date) {
        if (date == 0) {
            date = now();
        }
        StringBuilder jsonB = new StringBuilder("{\"status\": 0,\"data\": {\"mapData\": [");
        List<ProvinceStats> provinceStatsList = provinceStatsService.getProvinceStats(date);
        for (int i = 0; i < provinceStatsList.size(); i++) {
            ProvinceStats provinceStats = provinceStatsList.get(i);
            jsonB.append("{\"name\": \"" + provinceStats.getProvince_name() + "\",\"value\": " + provinceStats.getOrder_amount() + "}");
            if (i < provinceStatsList.size() - 1) {
                jsonB.append(",");
            }
        }

        jsonB.append("],\"valueName\": \"交易额\"}}");
        return jsonB.toString();
    }

    @RequestMapping("/spu")
    public String getProductStatsBySpu(
        @RequestParam(value = "date", defaultValue = "0") Integer date,
        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        if (date == 0) {
            date = now();
        }
        StringBuilder jsonB = new StringBuilder("{\"status\": 0,\"data\": {" +
            "\"columns\": [" +
            "{\"name\": \"商品名称\",\"id\": \"name\"}," +
            "{\"name\": \"交易额\",\"id\": \"amount\"}," +
            "{\"name\": \"订单数\",\"id\": \"ct\"}],\"rows\": [");
        List<ProductStats> productStatsList = productStatsService.getProductStatsBySpu(date, limit);
        for (int i = 0; i < productStatsList.size(); i++) {
            ProductStats productStats = productStatsList.get(i);
            if (i >= 1) {
                jsonB.append(",");
            }
            jsonB.append("{\"name\": \"" + productStats.getSpu_name() + "\",\"amount\": " + productStats.getOrder_amount() + ",\"ct\": " + productStats.getOrder_ct() + "}");
        }
        jsonB.append("]}}");
        return jsonB.toString();
    }

    //使用面向对象的方式处理json字符串的拼接         {}---Map        []---List
    @RequestMapping("/category3")
    public Map getProductStatsByCategory3(
        @RequestParam(value = "date", defaultValue = "0") Integer date,
        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        if (date == 0) {
            date = now();
        }
        //调用service方法，获取类别对应的交易额
        List<ProductStats> productStatsList = productStatsService.getProductStatsByCategory3(date, limit);

        /*{
            "status": 0,
            "data": [
            {
                "name": "PC",
                "value": 97
            },
            {
                "name": "iOS",
                "value": 50
            }
		  ]
        }*/
        Map resMap = new HashMap();
        resMap.put("status", 0);
        List dataList = new ArrayList();
        for (ProductStats productStats : productStatsList) {
            Map dataMap = new HashMap();
            dataMap.put("name", productStats.getCategory3_name());
            dataMap.put("value", productStats.getOrder_amount());
            dataList.add(dataMap);
        }

        resMap.put("data", dataList);
        return resMap;
    }
    /*@RequestMapping("/category3")
    public String getProductStatsByCategory3(
        @RequestParam(value = "date", defaultValue = "0") Integer date,
        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        if (date == 0) {
            date = now();
        }
        //调用service方法，获取类别对应的交易额
        List<ProductStats> productStatsList = productStatsService.getProductStatsByCategory3(date, limit);
        StringBuilder jsonB = new StringBuilder("{\"status\": 0,\"data\": [");
        for (int i = 0; i < productStatsList.size(); i++) {
            ProductStats productStats = productStatsList.get(i);
            jsonB.append(" {\"name\": \"" + productStats.getCategory3_name() + "\",\"value\": " + productStats.getOrder_amount() + "}");
            if(i < productStatsList.size() - 1){
                jsonB.append(",");
            }
        }
        jsonB.append("]}");
        return jsonB.toString();
    }*/

    @RequestMapping("/tm")
    public String getProductStatsByTm(
        @RequestParam(value = "date", defaultValue = "0") Integer date,
        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {

        if (date == 0) {
            date = now();
        }

        //调用service层的方法，获取品牌交易额
        List<ProductStats> productStatsList = productStatsService.getProductStatsByTm(date, limit);
        List tmList = new ArrayList();
        List amountList = new ArrayList();
        for (ProductStats productStats : productStatsList) {
            tmList.add(productStats.getTm_name());
            amountList.add(productStats.getOrder_amount());
        }
        String json = "{\"status\":0,\"data\":{" +
            "\"categories\":[\"" + StringUtils.join(tmList, "\",\"") + "\"]," +
            "\"series\":[{\"name\":\"商品品牌\",\"data\":[" + StringUtils.join(amountList, ",") + "]}]}}";

        return json;
    }

    @RequestMapping("/gmv")
    public String getGMV(@RequestParam(value = "date", defaultValue = "0") Integer date) {
        //如果没有传递日期参数，那么会将当天日期赋值给date变量
        if (date == 0) {
            date = now();
        }
        BigDecimal gmv = productStatsService.getGMV(date);
        String json = "{\"status\": 0,\"data\": " + gmv + "}";
        return json;
    }

    //获取当天日期
    private Integer now() {
        String yyyyMMdd = DateFormatUtils.format(new Date(), "yyyyMMdd");
        return Integer.valueOf(yyyyMMdd);
    }
}
