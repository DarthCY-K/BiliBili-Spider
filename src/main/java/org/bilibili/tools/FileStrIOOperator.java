package org.bilibili.tools;

import java.io.*;

public class FileStrIOOperator {

    public static String FileStrReaderWithPath(String filePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder strBuilder = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null){
                    break;
                }
                strBuilder.append(line+"\r\n");
            }
            if(bufferedReader!=null) bufferedReader.close();
            return strBuilder.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Boolean FileStrWriterWithData(String data, String fileDirectory, String fileName) {
        try {
            //准备一个临时文件夹
            File file = new File(fileDirectory);
            if(!file.exists() || !file.isDirectory()){
                file.mkdir();
            }
            FileOutputStream out = null;
            out = new FileOutputStream(fileDirectory+"\\"+fileName);
            out.write(data.getBytes());
            out.flush();

            if(out!=null) out.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
