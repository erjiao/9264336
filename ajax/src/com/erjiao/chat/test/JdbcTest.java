package com.erjiao.chat.test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

import com.erjiao.chat.dao.MessageDao;
import com.erjiao.chat.util.JDBCUtils;

public class JdbcTest {
	
	@Test
	public void test(){
		Connection conn = JDBCUtils.getConnection();
		System.out.println(conn);
	}
	
	@Test
	public void test2(){
		MessageDao dao = new MessageDao();
		System.out.println(dao.hasNew("1"));
	}
	
	@Test
	public void test3(){
		Timestamp timestamp = new Timestamp(new Date().getTime());
		System.out.println(timestamp);
	}
}
