package com.erjiao.chat.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erjiao.chat.bean.Message;
import com.erjiao.chat.dao.MessageDao;
import com.google.gson.Gson;

public class ServletGetNew extends HttpServlet {

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
		
		//1.获取finalMessageId
		String finalMessageId = request.getParameter("finalMessageId");
		
		//2.根据fianlMessageId获取新的聊天记录.
		MessageDao dao = new MessageDao();
		List<Message> list = dao.getNewMessage(finalMessageId);
		System.out.println(list);
		//3.转换为JSON字符串返回给浏览器.
		String json = new Gson().toJson(list);
		response.setContentType("text/json;charset=UTF-8");
		response.getWriter().write(json);
		
		System.out.println(json);
		
	}

}
