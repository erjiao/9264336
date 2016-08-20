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
		
		//1.从请求参数中获取查询关键字
		String queryStr = request.getParameter("queryStr");
		//2.根据查询关键字获取查询结果
		List<Company> queryList = CompanyDao.queryList(queryStr);
		//3.将查询结果转换为JSON字符串
		String json = new Gson().toJson(queryList);
		
		//4.将JSON字符串作为相应数据返回
		response.setContentType("text/json; charset=UTF-8");
		response.getWriter().write(json);
	}

}
