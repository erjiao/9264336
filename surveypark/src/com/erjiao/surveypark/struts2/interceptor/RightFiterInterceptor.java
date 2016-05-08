package com.erjiao.surveypark.struts2.interceptor;

import org.apache.struts2.ServletActionContext;

import com.erjiao.surveypark.struts2.action.BaseAction;
import com.erjiao.surveypark.util.ValidateUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 *	µÇÂ¼À¹½ØÆ÷ 
 */
public class RightFiterInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
//		BaseAction<?> action = (BaseAction<?>) arg0.getAction();
//		if (action instanceof LoginAction 
//				|| action instanceof RegAction) {
//			return arg0.invoke();
//		} else {
//			User user = (User) arg0.getInvocationContext().getSession().get("user");
//			if (null == user) {
//				//È¥µÇÂ½
//				return "login";
//			}
//			if (action instanceof UserAware) {
//				//×¢ÈëUser¸øAction
//				((UserAware) action).setUser(user);
//			}
//		}
//		//·ÅÐÐ
//		return arg0.invoke();
		BaseAction<?> action = (BaseAction<?>) arg0.getAction();
		ActionProxy proxy = arg0.getProxy();
		String ns = proxy.getNamespace();
		String actionName = proxy.getActionName();
		if (ValidateUtil.hasRight(ns, actionName, ServletActionContext.getRequest(), action)) {
			return arg0.invoke();
		}
		return "login";
	}

}
