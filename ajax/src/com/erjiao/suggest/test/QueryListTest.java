package com.erjiao.suggest.test;

import java.util.List;

import org.junit.Test;

import com.erjiao.suggest.bean.Company;
import com.erjiao.suggest.dao.CompanyDao;
import com.google.gson.Gson;

public class QueryListTest {
	
	@Test
	public void test() {
		
		List<Company> queryList = CompanyDao.queryList("jquery");
		for (Company company : queryList) {
			System.out.println(company);
		}
		
		//将查询结果的List集合对象转换为JSON字符串.
		String json = new Gson().toJson(queryList);
		System.out.println(json);
	}
	
}
