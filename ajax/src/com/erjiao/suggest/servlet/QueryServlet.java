package com.erjiao.suggest.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erjiao.suggest.bean.Company;
import com.erjiao.suggest.dao.CompanyDao;
import com.google.gson.Gson;

public class QueryServlet extends HttpServlet {

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
		
		//1.����������л�ȡ��ѯ�ؼ���
		String queryStr = request.getParameter("queryStr");
		//2.���ݲ�ѯ�ؼ��ֻ�ȡ��ѯ���
		List<Company> queryList = CompanyDao.queryList(queryStr);
		//3.����ѯ���ת��ΪJSON�ַ���
		String json = new Gson().toJson(queryList);
		
		//4.��JSON�ַ�����Ϊ��Ӧ���ݷ���
		response.setContentType("text/json; charset=UTF-8");
		response.getWriter().write(json);
	}

}
