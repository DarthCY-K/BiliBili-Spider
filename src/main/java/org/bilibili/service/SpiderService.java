package org.bilibili.service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bilibili.mappers.VideoMapper;
import org.bilibili.tools.AnalysisDownloadPageTool;
import org.bilibili.tools.DownLoadPageTools;
import org.bilibili.tools.FileStrIOOperator;
import org.bilibili.tools.ReadPageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpiderService {

    @Autowired
    private VideoMapper videoMapper;

    public List<HashMap<String, Object>> grabViewVideoList(String path) {
        try {
            String json = FileStrIOOperator.FileStrReaderWithPath(path);
            HashMap map = new ObjectMapper().readValue(json, HashMap.class);
            // 从map中取出电影信息列表
            ArrayList<HashMap<String, String>> viewVideos = (ArrayList<HashMap<String, String>>) map.get("subjects");
            List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
            for (HashMap<String, String> viewVideo : viewVideos) {
                // 获取 URL
                String viewVideoURL = viewVideo.get("link");
                viewVideoURL = "Https://" + viewVideoURL;
                // 获取 TITLE
                String viewVideoTitle = viewVideo.get("title");
                viewVideoTitle = viewVideoTitle.replace("/", "-").replace("\\", "-");
                // 获取 Type
                String viewVideoType = viewVideo.get("type");
                // 构造路径
                String viewVideoPagePath = "downloads\\pages\\" + viewVideoTitle.replace("*", "-").replace("/", "-").
                        replace("\\", "-").replace("|", "-").replace("<", "-").
                        replace(">", "-").replace(":","-").replace("?", "-")+".html";
                // 使用工具类直接下载
                DownLoadPageTools.DownLoadPageWithInflateGzip(viewVideoURL, viewVideoPagePath, new HashMap<>(), "utf-8");
                // 分析已下载页面并键入数据库
                HashMap<String, Object> result = AnalysisDownloadPageTool.AnalysisDownLoadPageWithPath(viewVideoPagePath, "utf-8");
                if(result == null){
                    return null;
                }
                resultList.add(result);
                System.out.println(result);
                try{
                    List<HashMap<String, Object>> findRepeat = videoMapper.QueryViewVideosItemWithMainKey(result.get("bTitle").toString(), result.get("bAuthor").toString(), result.get("bUpTime").toString());
                    if(findRepeat == null || findRepeat.size() == 0) {
                        result.put("bType", viewVideoType);
                        result.put("bLink", viewVideoURL.substring(viewVideoURL.lastIndexOf("BV")));
                        videoMapper.InsertViewVideos(result);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("SpiderService:::" + viewVideoType);
                    System.out.println("SpiderService:::" + viewVideoURL);
                    System.out.println("SpiderService:::" + result.get("bTitle").toString());
                    System.out.println("SpiderService:::" + result.get("bAuthor").toString());
                    System.out.println("SpiderService:::" + result.get("bUpTime").toString());

                }
            }
            return resultList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 抓取电影列表信息
     * @throws IOException
     */
    public void grabVideoList(String keyWord, int pageLimit) {
        for (int i = 1; i <= pageLimit; i++) {
            //创建一个HttpClient对象
            HttpClient httpClient = HttpClientBuilder.create().build();
            //稍微等1.8秒钟
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //计算开始位置d
            String url = "https://search.bilibili.com/all?keyword="
                    + keyWord +
                    "&from_source=web_search&page=" + i;
            System.out.println(url);
            //根据url创建一个HttpRequest    HttpGet是HttpRequest的一个实现类。
            HttpGet httpGet = new HttpGet(url);
            //设置请求头
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
            httpGet.addHeader("Host", "search.bilibili.com");
            httpGet.addHeader("accept-language","zh-CN,zh;q=0.9");

            try{
                //发送请求，得到响应体
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                //获取字节输入流
                InputStream in = entity.getContent();
                //将字节输入流转换为字符输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while (true) {
                    line = reader.readLine();
                    sb.append(line);
                    if (line.endsWith("</html>")) {
                        break;
                    }
                }
                ReadPageTools.vedioListToJson(i, keyWord, sb.toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

