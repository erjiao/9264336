package com.erjiao.surveypark.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestDataSource {
	
	@Test
	public void testConnection() throws SQLException {
		
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		DataSource ds = (DataSource) ac.getBean("dataSource-main");
		
		System.out.println(ds.getConnection());
		
	}
	
}
