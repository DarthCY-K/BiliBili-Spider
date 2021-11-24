package org.bilibili.test;

import java.io.File;

import org.bilibili.BilibiliSpiderApplication;
import org.bilibili.mappers.VideoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试数据存储
 * @author 戴着假发的程序员
 *
 */
@SpringBootTest(classes= BilibiliSpiderApplication.class)
@RunWith(SpringRunner.class)
public class MyBatisTest {
	
	@Autowired
	private VideoMapper mapper;

	@Test
	public void testeParseMovie() {
			//循环的测试10部电影的解析
			File file = new File("pages");
			String[] fileNames = file.list();
			int i =0;
			for(String fileName : fileNames) {
				try {
//					HashMap<String, String> result = new MovieDetailParseTools().parseDetail(fileName);
//					System.out.println(result);
//					//将这个result存储到数据库
//					mapper.saveMovie(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	}
}
