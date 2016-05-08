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
 * ��¼
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private RightService rightService;
	
	//���� session ��Map
	private Map<String, Object> sessionMap;
	
	/**
	 * �����¼ҳ��
	 * @return
	 */
	public String toLoginPage() {
		return "loginPage";
	}
	
	/**
	 * ���е�¼����
	 */
	public String doLogin() {
		return SUCCESS;
	}
	
	/**
	 * У���¼��Ϣ
	 */
	public void validateDoLogin() {
		User user = userService.validateLoginInfo(model.getEmail(), DataUtil.md5(model.getPassword()));
		if (user == null) {
			addActionError("email/password����!");
		} else {
			//������ʹ�ã� ��϶ȸ�.
			//ServletActionContext.getRequest().getSession().setAttribute("user", user);
			
			//��ʼ��Ȩ���ܺ�����
			int maxPos = rightService.getMaxRightPos();
			user.setRightSum(new long[maxPos +  1]);
			user.calculateRightSum();
			sessionMap.put("user", user);
		}
		
	}
	
	//ע��session ��Map
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}
	
}
