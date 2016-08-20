package com.erjiao.ajax.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxResponseJSONP extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/json;charset=UTF-8");
		//response.setCharacterEncoding("UTF-8");
		
		String callback = request.getParameter("callback");
		System.out.println(callback);
		System.out.println(callback + "({result : 1})");
		response.getWriter().write(callback + "({result : \"Œ“œ≤ª∂ƒ„\"})");
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
