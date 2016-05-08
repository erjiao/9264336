package com.erjiao.surveypark.struts2.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.RightService;
import com.erjiao.surveypark.service.UserService;
import com.erjiao.surveypark.util.DataUtil;

/**
 * 登录
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private RightService rightService;
	
	//接收 session 的Map
	private Map<String, Object> sessionMap;
	
	/**
	 * 到达登录页面
	 * @return
	 */
	public String toLoginPage() {
		return "loginPage";
	}
	
	/**
	 * 进行登录处理
	 */
	public String doLogin() {
		return SUCCESS;
	}
	
	/**
	 * 校验登录信息
	 */
	public void validateDoLogin() {
		User user = userService.validateLoginInfo(model.getEmail(), DataUtil.md5(model.getPassword()));
		if (user == null) {
			addActionError("email/password错误!");
		} else {
			//不建议使用， 耦合度高.
			//ServletActionContext.getRequest().getSession().setAttribute("user", user);
			
			//初始化权限总和数组
			int maxPos = rightService.getMaxRightPos();
			user.setRightSum(new long[maxPos +  1]);
			user.calculateRightSum();
			sessionMap.put("user", user);
		}
		
	}
	
	//注入session 的Map
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}
	
}
