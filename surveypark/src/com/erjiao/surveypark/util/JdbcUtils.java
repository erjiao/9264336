package com.erjiao.surveypark.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtils {
	
	private static String dbDriver = "oracle.jdbc.OracleDriver";
	private static String dbUrl = "jdbc:oracle:thin:@localhost:1521:orcl1";
	private static String dbName = "scott";
	private static String dbPwd = "tiger";
	
	private static Connection connection = null;
	
	static{
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			System.out.println("��������ʧ�ܣ�");
			e.printStackTrace();
		}
	};
	
	public static Connection getConnection(){
		
		try {
			connection = DriverManager.getConnection(dbUrl,dbName,dbPwd);
		} catch (SQLException e) {
			System.out.println("�������ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return connection;
	}

}
