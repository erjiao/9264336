package com.erjiao.ajax.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxResponseJSON extends HttpServlet {

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
		
		//×¼±¸JSON ×Ö·û´®
		String jsonStr = "[\"hello\", {userName : \"erjiao\"}]";
		response.setContentType("text/json;charset=UTF-8");
		
		PrintWriter writer = response.getWriter();
		writer.write(jsonStr);
		
	}

}
