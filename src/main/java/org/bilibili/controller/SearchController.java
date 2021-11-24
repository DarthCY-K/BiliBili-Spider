package org.bilibili.controller;

import org.apache.ibatis.annotations.Param;
import org.bilibili.mappers.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DarthCY
 */
@Controller
public class SearchController {
    @Autowired
    private VideoMapper videoMapper;

    @RequestMapping("/doSearch")
    @ResponseBody
    public Map<String,Object> doSearch(
            @RequestParam(defaultValue="1")int page,
            @RequestParam(defaultValue="10")int limit,
            String bTitle, String bType, String bAuthor,
            @Param("bLike") String bLikeMin, @Param("bLike") String bLikeMax,
            @Param("bCoin") String bCoinMin, @Param("bCoin") String bCoinMax,
            @Param("bCollect") String bCollectMin, @Param("bCollect") String bCollectMax,
            @Param("bShare") String bShareMin, @Param("bShare") String bShareMax,
            @Param("bViews") String bViewsMin, @Param("bViews") String bViewsMax,
            @Param("bBarrages") String bBarragesMin, @Param("bBarrages") String bBarragesMax,
            @Param("bUpTime") String bUpTime) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","查询完成");
        //总条数  从数据库查询总条数   查询条数的时候也要携带查询条件
        int total = videoMapper.queryVideoTotal(bTitle,bType,bAuthor,bLikeMin,bLikeMax,
                bCoinMin,bCoinMax,bCollectMin,bCollectMax,bShareMin,bShareMax,
                bViewsMin,bViewsMax,bBarragesMin,bBarragesMax,bUpTime);
        System.out.println("SearchController:::查询到的数据总条数为 " + total);
        map.put("count", total);
        /*
        查询数据列表
        这里查询的时候，需要传入参数
        计算开始位置
         */
        int start = (page-1)*limit;
        //调用mapper查询    查询的时候要携带查询条件
        List<Map<String, Object>> list = videoMapper.searchVideo(start,limit,bTitle,bType,bAuthor,bLikeMin,bLikeMax,
                bCoinMin,bCoinMax,bCollectMin,bCollectMax,bShareMin,bShareMax,
                bViewsMin,bViewsMax,bBarragesMin,bBarragesMax,bUpTime);
        map.put("data",list);
        return map;
    }
}
