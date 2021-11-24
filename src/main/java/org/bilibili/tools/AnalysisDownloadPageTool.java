package org.bilibili.tools;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class AnalysisDownloadPageTool {

    public static HashMap<String, Object> AnalysisDownLoadPageWithPath(String filePath, String charSet) {
        try {
            Document doc = Jsoup.parse(new File(filePath), charSet);
            HashMap<String, Object> result = new HashMap<String, Object>();

            String bTitle = doc.select(".video-title").attr("title");
            String bAuthor = doc.select("[itemprop=\"author\"]").attr("content");

            String strLs = "";

            // 获取点赞数
            strLs = doc.select(".like").text();
            int bLike = 0;
            if(strLs.length() > 0 && strLs.endsWith("万"))
                bLike = (int)(Float.parseFloat(strLs.substring(0, strLs.length() - 1)) * 10000);
            else if(strLs.equals("点赞")) {
                bLike = 0;
            }
            else {
                try {
                    bLike = Integer.parseInt(strLs);
                }catch (Exception e) {
                    System.out.println(filePath+"：点赞转换失败");
                    bLike = 0;
                }
            }
            // 获取投币数
            strLs = doc.select(".coin").text();
            int bCoin = 0;
            if(strLs.length() > 0 && strLs.endsWith("万"))
                bCoin = (int)(Float.parseFloat(strLs.substring(0, strLs.length() - 1)) * 10000);
            else if(strLs.equals("投币")){
                bCoin = 0;
            }
            else {
                try {
                    bCoin = Integer.parseInt(strLs);
                }catch (Exception e) {
                    System.out.println(filePath+"：投币转换失败");
                    bCoin = 0;
                }
            }
            // 获取收藏数
            strLs = doc.select(".collect").text();
            int bCollect = 0;
            if(strLs.length() > 0 && strLs.endsWith("万"))
                bCollect = (int)(Float.parseFloat(strLs.substring(0, strLs.length() - 1)) * 10000);
            else if(strLs.equals("收藏")){
                bCollect = 0;
            }
            else {
                try {
                    bCollect = Integer.parseInt(strLs);
                }catch (Exception e) {
                    System.out.println(filePath+"：收藏转换失败");
                    bCollect = 0;
                }
            }
            // 获取分享数
            strLs = doc.select(".share").text();
            int bShare = 0;
            if(strLs.length() > 0 && strLs.endsWith("万"))
                bShare = (int)(Float.parseFloat(strLs.substring(0, strLs.length() - 1)) * 10000);
            else if(strLs.equals("分享")) {
                bShare = 0;
            }
            else {
                try {
                    bShare = Integer.parseInt(strLs);
                }catch (Exception e) {
                    System.out.println(filePath+"：分享转换失败");
                    bShare = 0;
                }
            }
            // 获取观看数
            int bViews = Integer.parseInt(doc.select(".view").attr("title").substring(4));
            // 获取弹幕数
            int bBarrages = Integer.parseInt(doc.select(".dm").attr("title").substring(7));
            Element element = doc.select(".dm").first().nextElementSibling();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date bUpTime = simpleDateFormat.parse(element.text());

            result.put("bTitle", String.valueOf(bTitle));
            result.put("bAuthor", String.valueOf(bAuthor));
            result.put("bLike", bLike);
            result.put("bCoin", bCoin);
            result.put("bCollect", bCollect);
            result.put("bShare", bShare);
            result.put("bViews", bViews);
            result.put("bBarrages", bBarrages);
            result.put("bUpTime", String.valueOf(simpleDateFormat.format(bUpTime)));

            return result;

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(filePath);
        }
        return null;
    }
}
