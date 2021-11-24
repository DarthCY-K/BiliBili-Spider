package org.bilibili.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 45271
 */
public class ReadPageTools {

    //获取列表视频信息函数
    public static void vedioListToJson(int num, String keyWord, String str){
        String type = null, title = null, link = null;
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String,String>> mapList = new ArrayList<>();

        Document doc = Jsoup.parse(str);
        Elements list = doc.getElementsByClass("headline clearfix");

        for(int i = 0;i < 20;i++){
            Element element = list.get(i);
            String elementStr = element.toString();

            link = elementStr.substring(elementStr.indexOf("href=\"//") + 8, elementStr.indexOf("?from=search"));
            type = elementStr.substring(elementStr.indexOf("<span class=\"type hide\">") + 24, elementStr.indexOf("</span>"));
            title = elementStr.substring(elementStr.indexOf("<a title=") + 10, elementStr.indexOf("\" href=\""));

            HashMap<String,String> map = new HashMap<>();
            map.put("title", title);map.put("type", type);map.put("link", link);
            mapList.add(map);
        }

        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(mapList);
            jsonStr = "{\"subjects\":" + jsonStr + "}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        DataSaveTools.saveJson(jsonStr, keyWord + "-" + num + ".json");
    }
}
