package com.erjiao.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class AjaxjQueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String name = request.getParameter("name");
		System.out.println(name);
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("password", "9264336");
		
		//3.调用gson对象的toJson(Object src)方法将对象转换为JSON字符串输出
		String jsonStr = new Gson().toJson(map);
		
		//4.设置响应的内容类型
		response.setContentType("text/josn;charset=UTF-8");
		
		//5.获取用于返回数据的字符输出流
		PrintWriter writer = response.getWriter();
		
		//6.返回响应数据
		writer.write(jsonStr);
	}

}
