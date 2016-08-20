package com.erjiao.chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erjiao.chat.bean.Message;
import com.erjiao.chat.dao.MessageDao;

public class SendMessage extends HttpServlet {

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
		
		//1.获取聊天记录的值.
		String msg = request.getParameter("message");
		System.out.println(msg);
		
	    Message message = new Message(null, msg, new Date());
		
		MessageDao dao = new MessageDao();
		dao.saveMessage(message);
		
	}

}
