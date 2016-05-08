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
 * ע�� action
 */
@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {
	
	private static final long serialVersionUID = 1L;
	
	//ģ�Ͷ�����Ҫ��ʼ��,struts ��ģ�������������ж�ģ���Ƿ�Ϊ��, ��Ϊ�ղ�ѹ��ջ��, Ϊ�ղ�ѹ.
	//private User model = new User();
	
	private String confirmPassword;
	
	//ע�� userService
	@Resource
	private UserService userService;
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * ����ע��ҳ��
	 * @return
	 */
	//����У��
	@SkipValidation
	public String toRegPage(){
		return "regPage";
	}
	
	/**
	 * �����û�ע��
	 */
	public String doReg(){
		//�������
		model.setPassword(DataUtil.md5(model.getPassword()));
		userService.saveEntity(model);
		return SUCCESS;
	}
	
	/**
	 * У��
	 */
	public void validate() {
		//1.�ǿ�
		if (!ValidateUtil.isValid(model.getEmail())) {
			addFieldError("email", "email�Ǳ�����!");
		}
		if (!ValidateUtil.isValid(model.getPassword())) {
			addFieldError("password", "password�Ǳ�����!");
		}
		if (!ValidateUtil.isValid(model.getNickName())) {
			addFieldError("nickName", "nickName�Ǳ�����!");
		}
		
		if (hasErrors()) {
			return;
		}
		
		//2.����һ����
		System.out.println(confirmPassword);
		if (!model.getPassword().equals(confirmPassword)) {
			addFieldError("password", "���벻һ��!");
		}
		
		//3.email ռ��
		if (userService.isRegisted(model.getEmail())) {
			addFieldError("email", "email��ռ��!");
		} 
	}

}
