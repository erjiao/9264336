package com.erjiao.ajax.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {
	
	@Test
	public void testGson() {
		List<Object> list = new ArrayList<Object>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "erjiao");
		map.put("password", "9264336");
		list.add("11111");
		list.add("22222");
		list.add(new Date());
		
		GsonBuilder gson2 = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = new Gson();
		String json = gson2.create().toJson(list);
		
		System.out.println(json);
		System.out.println(map);
		
		StringBuffer b = new StringBuffer();
		b.append(1).append("2");
		System.out.println(b.toString());
		
	}
}
