package com.erjiao.surveypark.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.struts2.UserAware;
import com.erjiao.surveypark.struts2.action.BaseAction;

public class ValidateUtil {
	
	/**
	 * �ж��ַ�����Ч��.
	 */
	public static boolean isValid(String src){
		return !(src == null || "".equals(src.trim()));
	}
	
	/**
	 * �жϼ��ϵ���Ч��. 
	 */
	public static boolean isValid(Collection<?> coll) {
		return !(coll == null || coll.isEmpty());
	}
	
	/**
	 * �ж��������Ч��
	 */
	public static boolean isValid(Object[] arr) {
		return !(arr == null || arr.length == 0);
	}
	
	/**
	 * �ж��Ƿ���Ȩ��
	 */
	public static boolean hasRight(String namespace, String actionName, HttpServletRequest req, BaseAction<?> action) {
		if (ValidateUtil.isValid(namespace)
				||"/".equals(namespace)) {
			namespace = "";
		}
		//�������ӵĲ������ֹ��˵�
		if (actionName.contains("?")) {
			actionName = actionName.substring(0, actionName.indexOf("?"));
		}
		String url = namespace + "/" + actionName;
		HttpSession session = req.getSession();
		ServletContext sc = req.getServletContext();
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		Right r = map.get(url);
		if (null == r || r.isCommon()) {
			return true;
		} else {
			User user = (User) session.getAttribute("user");
			//��¼
			if (null == user) {
				return false;
			} else {
				//userAware ����
				if (null != action && action instanceof UserAware) {
					((UserAware) action).setUser(user);
				}
				if (user.isSuperAdmin()) {
					return true;
				} else {
					//��Ȩ��
					if (user.hasRight(r)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
	}
	
}
