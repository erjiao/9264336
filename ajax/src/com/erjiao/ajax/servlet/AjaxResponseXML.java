package com.erjiao.ajax.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxResponseXML extends HttpServlet {

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
		//׼��XML����, ��Ϊ��Ӧ���ظ������.
		response.setContentType("text/xml;chartset=UTF-8");
		String xmlStr = "<user><username>erjiao</username></user>";
		response.getWriter().write(xmlStr);
	}

}
