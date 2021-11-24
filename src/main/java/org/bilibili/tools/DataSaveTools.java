package org.bilibili.tools;

import java.io.File;
import java.io.FileOutputStream;

public class DataSaveTools {
	//存储json数据到文件中
	public static void saveJson(String json,String fileName) {
		//准备一个临时文件夹
		File file = new File("downloads/jsons");
		if(!file.exists() || !file.isDirectory())
			file.mkdir();
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("downloads\\jsons\\"+fileName);
			System.out.println("DataSaveTools-JsonSave:::"+json);
			out.write(json.getBytes());
			out.flush();
		} catch (Exception e) {
			e.getSuppressed();
		}finally {
			try {
				if(out!=null)
					out.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
