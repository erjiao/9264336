package com.erjiao.surveypark.struts2.interceptor;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.erjiao.surveypark.service.RightService;
import com.erjiao.surveypark.util.ValidateUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 捕获 url 拦截器 
 */
public class CatchUrlInterceptor implements Interceptor {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {}

	@Override
	public void init() {}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		//名字空间
		String ns = proxy.getNamespace();
		if (!ValidateUtil.isValid(ns) || ns.equals("/")) {
			ns = "";
		}
		//actionName
		String actionName = proxy.getActionName();
		String url = ns + "/" + actionName;
		//取得在 application 中的 spring容器
		//ApplicationContext ac = (ApplicationContext) invocation.getInvocationContext().getApplication().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		ServletContext sc = ServletActionContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		RightService rs = (RightService) ac.getBean("rightService");
		
		rs.appendRightByURL(url);
		return invocation.invoke();
	}

}
