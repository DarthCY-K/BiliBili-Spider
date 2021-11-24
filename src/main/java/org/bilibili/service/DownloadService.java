package org.bilibili.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bilibili.tools.BvAvTool;
import org.bilibili.tools.DownLoadPageTools;
import org.bilibili.tools.FileStrIOOperator;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DarthCY
 */
public class DownloadService {
    private static String bTitle;
    static long start = 0;
    static long end = 0;

    public static void execute(String bvid) {
        start = System.currentTimeMillis();
        //输入av号
        String avid = BvAvTool.convert("b2v",bvid);
        //建立连接，先获取到 cid
        String cidJson = createConnectionToJson(avid.substring(2));
        String cid = jsonGetCid(cidJson);
        // 根据cid拼接成完整的请求参数,并执行下载操作
        downloadMovie(avid.substring(2), cid);
        end = System.currentTimeMillis();
        System.err.println("总共耗时：" + (end - start) / 1000 + "s");
    }


    //建立连接拿到 json 数据
    public static String createConnectionToJson(String avid) {
        String cidUrl = "https://api.bilibili.com/x/web-interface/view?aid=" + avid;
        //放完 movie地址
        String cidJson = createConnection(cidUrl);
        return cidJson;
    }

    //获取到的json选择出cid，只能选择出一个cid，还有标题
    public static String jsonGetCid(String cidJson) {
        //转换成json
        JSONObject jsonObject = JSONObject.parseObject(cidJson);
        //cid
        JSONObject jsonData = jsonObject.getJSONObject("data");

        JSONArray jsonArray = jsonData.getJSONArray("pages");
        Map<String, Object> pageMap = (Map) jsonArray.get(0);
        int cid = (Integer) pageMap.get("cid");
        System.out.println("cid: " + cid);
        //title
        bTitle = jsonData.getString("title");
        System.out.println("title:" + bTitle);
        return String.valueOf(cid);
    }

    //下载页面,返回页面中的json
    public static String createConnection(String url) {
        String jsonText = null;
        DownLoadPageTools.DownLoadPage(url, "downloads\\123.json", new HashMap<>(), "utf-8");
        jsonText = FileStrIOOperator.FileStrReaderWithPath("downloads\\123.json");
        return jsonText;
    }

    //下载视频
    public static void downloadMovie( String avid,  String cid) {
        //qn ： 视频质量         112 -> 高清 1080P+,   80 -> 高清 1080P,   64 -> 高清 720P,  32  -> 清晰 480P,  16 -> 流畅 360P
        // 最高支持 1080p,  1080P+是不支持的
        Integer qn = 80;
        String paraUrl = "https://api.bilibili.com/x/player/playurl?cid=" + cid + "&fnver=0&qn=" + qn + "&otype=json&avid=" + avid + "&fnval=2&player=1";
        System.out.println("构建的url为：" + paraUrl);
        // 获取到的是json，然后筛选出里面的视频资源：url
        String jsonText = createConnection(paraUrl);

        JSONObject jsonObject = JSONObject.parseObject(jsonText);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("durl");

        Map<String, String> dUrlMap = (Map) jsonArray.get(0);
        String movieUrl = dUrlMap.get("url");

        System.out.println("视频资源url为：" + movieUrl);
        // 根据获取的title 创建文件
        String moviePath = createMoviePath(bTitle);
        //建立连接
        InputStream inputStream = createInputStream(movieUrl,avid);
        //开始流转换
        inputStreamToFile(inputStream, moviePath);
    }

    // 3-2  建立URL连接请求
    private static InputStream createInputStream(String movieUrl,  String avid) {
        InputStream inputStream = null;
        long start = System.currentTimeMillis();
        try {
            URL url = new URL(movieUrl);
            URLConnection urlConnection = url.openConnection();
            String refererUrl = "https://www.bilibili.com/video/av" + avid;
            urlConnection.setRequestProperty("Referer",refererUrl );
            urlConnection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            urlConnection.setConnectTimeout(10 * 1000);

            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("获取inputStream失败");
        }
        return inputStream;
    }


    // 创建视频路径
    public static String createMoviePath(String title) {
        System.out.println("开始创建视频路径");
        //图片名称
        String movieName = title + ".flv";
        //创建路径
        String path = "downloads\\videos";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = dir + File.separator + movieName;
        System.out.println("视频路径：" + fileName);
        return fileName;
    }

    public static void inputStreamToFile(InputStream inputStream, String moviePath) {
        int i = 1;
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(moviePath));
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
                System.out.println("写入第 " + i++ + "次" );
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("inputStream转换异常");
        }

    }
}