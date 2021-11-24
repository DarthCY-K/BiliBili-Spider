package org.bilibili.tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 *  下载网页的工具类
 * @author 戴着假发的程序员
 *
 */
public class DownLoadPageTools {
	/**
	 * 下载网页
	 * @param url 要下载的网页地址
	 * @param pagePath 保存到本地的时候的网页文件的名字
	 * @param headers 请求头 {User-Agent:"M...",Accept:"*\*",Cookie:"XXXXXX"}
	 * @param charSet 网页的字符集
	 * @return 1 表示下载完成，0表示下载失败
	 */
	public static boolean DownLoadPage(String url, String pagePath, Map<String,String> headers, String charSet){
		try {
			URL url_ = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) url_.openConnection();
			// 重置头部
			if(!headers.containsKey("User-Agent")){
				headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.64");
			}
			if(!headers.containsKey("Accept")){
				headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			}
			if(!headers.containsKey("")) {
				headers.put("Cookie", "fingerprint=d496a8caaf0983a670ad5fe0a314fb5b; buvid_fp=; buvid_fp_plain=E9ABF2DC-6245-4CF4-99B5-3E19B41A8DD3155842infoc; SESSDATA=92ffd047%2C1640422044%2C00e42%2A61; bili_jct=cc9ecac993eef2194f937c5ed4e59430; DedeUserID=174035927; DedeUserID__ckMd5=d47f990c98db08a2; sid=ieixbnhk; fingerprint3=5521f1f6f5debce76e05d62edcd507ae; fingerprint_s=326b70700342fab41de17fa72dc9ab9c; buvid3=B33918B6-3744-0A2D-0BD9-7B9F2A316B6E88935infoc; CURRENT_FNVAL=80; blackside_state=1; rpdid=|(kl~Jll)Yum0J'uYklR)RJmu; LIVE_BUVID=AUTO8416254848669533; bp_t_offset_174035927=543931043668646058; _uuid=0AE77DB1-B46A-DA16-00F6-0C8B3908DB7703735infoc; PVID=1");
			}
			//设置请求头
			for(String key : headers.keySet()){
				connection.addRequestProperty(key, headers.get(key));
			}
			connection.connect();
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,charSet));
			FileWriter writer = new FileWriter(pagePath);
			//循环读写
			String line = null;
			while(true){
				line = reader.readLine();
				if(line==null ||line.trim().endsWith("</html>"))
					break;
				System.out.println(line);
				writer.write(line+"\r\n");
				writer.flush();//刷缓冲
			}
			if(reader!=null)
				reader.close();
			if(in!=null)
				in.close();
			if(writer!=null)
				writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param url
	 * @param pagePath
	 * @param headers
	 * @param charSet
	 * @return
	 */
	public static boolean DownLoadPageWithInflateGzip(String url, String pagePath, Map<String,String> headers, String charSet) {
		try {
			URL url_ = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) url_.openConnection();
			// 重置头部
			if(!headers.containsKey("User-Agent")){
				headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.64");
			}
			if(!headers.containsKey("Accept")){
				headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			}
			if(!headers.containsKey("")) {
				headers.put("Cookie", "fingerprint=d496a8caaf0983a670ad5fe0a314fb5b; buvid_fp=; buvid_fp_plain=E9ABF2DC-6245-4CF4-99B5-3E19B41A8DD3155842infoc; SESSDATA=92ffd047%2C1640422044%2C00e42%2A61; bili_jct=cc9ecac993eef2194f937c5ed4e59430; DedeUserID=174035927; DedeUserID__ckMd5=d47f990c98db08a2; sid=ieixbnhk; fingerprint3=5521f1f6f5debce76e05d62edcd507ae; fingerprint_s=326b70700342fab41de17fa72dc9ab9c; buvid3=B33918B6-3744-0A2D-0BD9-7B9F2A316B6E88935infoc; CURRENT_FNVAL=80; blackside_state=1; rpdid=|(kl~Jll)Yum0J'uYklR)RJmu; LIVE_BUVID=AUTO8416254848669533; bp_t_offset_174035927=543931043668646058; _uuid=0AE77DB1-B46A-DA16-00F6-0C8B3908DB7703735infoc; PVID=1");
			}
			//设置请求头
			for(String key : headers.keySet()){
				connection.addRequestProperty(key, headers.get(key));
			}
			connection.connect();
			InputStream in = connection.getInputStream();
			OutputStream out = new FileOutputStream(pagePath);
			// GZIP 解码
            GZIPInputStream gzipIn = new GZIPInputStream(in);
            int length = 0;
            byte[] contentByte = new byte[1024];
            while((length = gzipIn.read(contentByte))>0) {
                out.write(contentByte, 0, length);
            }
            if(in != null) in.close();
            if(out!=null) out.close();

            System.out.println("完成：" + pagePath);
			Thread.sleep(500);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
