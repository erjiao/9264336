package com.erjiao.chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erjiao.chat.dao.MessageDao;

public class ServletAsk extends HttpServlet {

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
		//1.获取请求参数： finalMessageId
		String finalMessageId = request.getParameter("finalMessageId");
		//System.out.println(finalMessageId);
		//2.根据finalMessageId 查询是否存在最新的聊天记录
		MessageDao messageDao = new MessageDao();
		boolean hasNew = messageDao.hasNew(finalMessageId);
		//3.将布尔类型的返回值以Ajax响应的形式返回给浏览器
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(String.valueOf(hasNew));
	}

}
