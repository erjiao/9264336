package com.erjiao.surveypark.struts2.action;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.UserService;
import com.erjiao.surveypark.util.DataUtil;
import com.erjiao.surveypark.util.ValidateUtil;

/**
 * 注册 action
 */
@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {
	
	private static final long serialVersionUID = 1L;
	
	//模型对象需要初始化,struts 的模型驱动拦截器判断模型是否为空, 不为空才压入栈顶, 为空不压.
	//private User model = new User();
	
	private String confirmPassword;
	
	//注入 userService
	@Resource
	private UserService userService;
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * 到达注册页面
	 * @return
	 */
	//跳过校验
	@SkipValidation
	public String toRegPage(){
		return "regPage";
	}
	
	/**
	 * 进行用户注册
	 */
	public String doReg(){
		//密码加密
		model.setPassword(DataUtil.md5(model.getPassword()));
		userService.saveEntity(model);
		return SUCCESS;
	}
	
	/**
	 * 校验
	 */
	public void validate() {
		//1.非空
		if (!ValidateUtil.isValid(model.getEmail())) {
			addFieldError("email", "email是必填项!");
		}
		if (!ValidateUtil.isValid(model.getPassword())) {
			addFieldError("password", "password是必填项!");
		}
		if (!ValidateUtil.isValid(model.getNickName())) {
			addFieldError("nickName", "nickName是必填项!");
		}
		
		if (hasErrors()) {
			return;
		}
		
		//2.密码一致性
		System.out.println(confirmPassword);
		if (!model.getPassword().equals(confirmPassword)) {
			addFieldError("password", "密码不一致!");
		}
		
		//3.email 占用
		if (userService.isRegisted(model.getEmail())) {
			addFieldError("email", "email已占用!");
		} 
	}

}
