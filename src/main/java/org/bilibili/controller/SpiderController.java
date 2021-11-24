package org.bilibili.controller;

import org.bilibili.mappers.VideoMapper;
import org.bilibili.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 45271
 */
@Controller
public class SpiderController {
    @Autowired
    private SpiderService spiderService;

    @Autowired
    private VideoMapper videoMapper;

    private static List<HashMap<String, Object>> recentSpiderFilled = null;
    private static String recentSpiderStr = null;

    @RequestMapping("/doSpider")
    @ResponseBody
    public HashMap<String,Object> doSpider(@RequestParam(defaultValue="1")int page, @RequestParam(defaultValue="10")int limit, @RequestParam(defaultValue="")String keyWord) throws IOException {
        //0.判定是否为检索
        if(recentSpiderStr==null || !recentSpiderStr.equals(keyWord)) {
            recentSpiderStr = keyWord;
            recentSpiderFilled = new LinkedList<HashMap<String, Object>>();
            //2.通过关键字在 BILIBILI 中搜索数据页面，并抓取一层页面，使用 JSON 保存到本地 "downloads\\jsons\\....json"
            spiderService.grabVideoList(keyWord, 50);
            //3.解析本地 "downloads\\jsons\\....json" 所有文件，提取页面保存到本地 "downloads\\pages\\....html"，生成缓存，并组织删除已解析使用的 JSON 文件
            File fileJsons = new File("downloads\\jsons");
            String[] fileJsonNames = fileJsons.list();
            for(String fileName : fileJsonNames) {
                try{
                    List<HashMap<String, Object>> listLs = spiderService.grabViewVideoList("downloads\\jsons\\"+fileName);
                    if(listLs != null){ recentSpiderFilled.addAll(listLs); }
                    new File("downloads\\jsons\\"+fileName).delete();
                }catch(Exception e){
                    System.out.println("SpiderController:::解析出错");
                }
            }
            //4.删除本地 "downloads\\pages\\....html" 文件
            File filePages = new File("downloads\\pages");
            String[] filePageNames = filePages.list();
            for(String fileName : filePageNames) {
                new File("downloads\\pages\\"+fileName).delete();
            }
        }
        //5.使用缓存返回数据
        return gotViewVideosCache(page, limit);
    }

    @RequestMapping("/gotViewVideosMapCache")
    @ResponseBody
    public HashMap<String,Object> gotViewVideosCache(@RequestParam(defaultValue="1")int page, @RequestParam(defaultValue="10")int limit) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","查询完成");
        //获取缓存总条目
        int total = 0;
        if(recentSpiderFilled!=null) total = recentSpiderFilled.size();
        map.put("count", total);
        List<HashMap<String, Object>> list = null;
        if(recentSpiderFilled!=null) {
            int start = (page-1)*limit;
            int end = start + limit;
            list = recentSpiderFilled.subList(start, end);
        }
        map.put("data",list);
        return map;
    }

    @RequestMapping("/gotViewVideosMap")
    @ResponseBody
    public HashMap<String,Object> gotViewVideos(@RequestParam(defaultValue="1")int page, @RequestParam(defaultValue="10")int limit) {
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("code",0);
        map.put("msg","查询完成");
        //总条数  从数据库查询总条数   查询条数的时候也要携带查询条件
        int total = videoMapper.QueryViewVideosNumber();
        map.put("count", total);
        List<Map<String, Object>> list = videoMapper.QueryViewVideosItem(page, limit);
        map.put("data",list);
        return map;
    }

    @RequestMapping("/gotAccumulationDistribution")
    @ResponseBody
    public List<List<Integer>> gotAccumulationDistribution(@RequestParam(defaultValue="2017")int startYear, @RequestParam(defaultValue="2021")int endYear) {

        List<List<Integer>> backList = new LinkedList<>();

        for (int  yearData= startYear; yearData<=endYear; yearData++) {

            List<Integer> list = new LinkedList<>();

            int viewsNum=0, barragesNum=0, likesNum=0, coinsNum=0, collectsNum=0, sharesNum=0;
            List<HashMap<String, Object>> gotList = videoMapper.QueryViewVideosIntegrateInformationWithBUpTime(String.valueOf(yearData));
            for(HashMap<String, Object> itemMap : gotList) {
                viewsNum += Integer.parseInt(itemMap.get("bViews").toString());
                barragesNum += Integer.parseInt(itemMap.get("bBarrages").toString());
                likesNum += Integer.parseInt(itemMap.get("bLike").toString());
                coinsNum += Integer.parseInt(itemMap.get("bCoin").toString());
                collectsNum += Integer.parseInt(itemMap.get("bCollect").toString());
                sharesNum += Integer.parseInt(itemMap.get("bShare").toString());
            }
            // 观看
            list.add(viewsNum);
            // 弹幕
            list.add(barragesNum);
            // 点赞
            list.add(likesNum);
            // 投币
            list.add(coinsNum);
            // 收藏
            list.add(collectsNum);
            // 转发
            list.add(sharesNum);
            backList.add(list);
        }
        return backList;
    }

    @RequestMapping("/gotDivergentDistribution")
    @ResponseBody
    public List<List<Object>> gotDivergentDistribution(@RequestParam(defaultValue="19") int maxLimit) {

        List<List<Object>> backList = new LinkedList<>();
        List<HashMap<String, Object>> gotList = videoMapper.QueryViewVideosBTypeInformationRemoveDuplication();
        int index = 0;
        // 将类型名称列表添加到返回链表
        List<Object> typeNameList = new LinkedList<Object>();
        List<Object> typeValueList = new LinkedList<Object>();
        backList.add(typeNameList);
        backList.add(typeValueList);
        for (HashMap<String, Object> itemMap : gotList) {

            String typeName = itemMap.get("bType").toString();
            int startIndex=0, endIndex = typeName.indexOf(" ");
            if(endIndex > startIndex) typeNameList.add(typeName.substring(startIndex, endIndex));
            else typeNameList.add(typeName);
            //typeNameList.add(typeName);
            // 依次将类型的三指列表添加到返回链表
            List<Object> typeValueValueList = new LinkedList<Object>();
            typeValueList.add(typeValueValueList);
            List<HashMap<String, Object>> typeSameItemList = videoMapper.QueryViewVideosIntegrateInformationWithBType(typeName);
            int minValue=2147483647, maxValue = 0;
            long sumValue = 0;
            double midValue = 0;

            for (HashMap<String, Object> iMap : typeSameItemList) {
                int[] nums = {
                        Integer.parseInt(iMap.get("bLike").toString()),
                        Integer.parseInt(iMap.get("bCoin").toString()),
                        Integer.parseInt(iMap.get("bCollect").toString()),
                        Integer.parseInt(iMap.get("bShare").toString()),
                        Integer.parseInt(iMap.get("bViews").toString()),
                        Integer.parseInt(iMap.get("bBarrages").toString())
                };
                for(int i=0; i<6; i++) {
                    if(minValue > nums[i]) minValue = nums[i];
                    if(maxValue < nums[i]) maxValue = nums[i];
                    sumValue += nums[i];
                }
            }
            midValue = sumValue*1.0/typeSameItemList.size()/6.0;
            typeValueValueList.add(minValue);
            typeValueValueList.add(maxValue);
            typeValueValueList.add(midValue);
            index++;
            if(index == maxLimit) break;
        }
        return backList;
    }

    @RequestMapping("/gotInterlacedDistribution")
    @ResponseBody
    public List<List<Object>> gotInterlacedDistribution(@RequestParam(defaultValue="2021")int yStart, @RequestParam(defaultValue="1")int mStart, @RequestParam(defaultValue="2021") int yEnd, @RequestParam(defaultValue="11") int mEnd) {
        List<List<Object>> backList = new LinkedList<>();

        for(int y=yStart; y<=yEnd; y++) {
            int ms = 1, me = 12;
            if(y==yStart) ms = mStart;
            if(y==yEnd) me = mEnd;
            for(int m=ms; m<=me; m++) {
                int ylen = String.valueOf(y).length();
                int mlen = String.valueOf(m).length();
                String strLs = "";
                for(int i=0, il=4-ylen; i<il; i++) { strLs += "0"; }
                strLs += y;
                strLs += "-";
                for(int i=0, il=2-mlen; i<il; i++) { strLs += "0"; }
                strLs += m;

                int count = 0;
                List<HashMap<String, Object>> gotList = videoMapper.QueryViewVideosIntegrateInformationWithBUpTime(strLs);
                if(gotList != null) {
                    count = gotList.size();
                }
                List<Object> inList = new LinkedList<>();
                inList.add(strLs);
                inList.add(count);
                backList.add(inList);
            }
        }

        return backList;
    }
}
