package com.erjiao.surveypark.service;

import com.erjiao.surveypark.model.User;

/**
 * UserService
 */
public interface UserService extends BaseService<User>{
	
	/**
	 * �ж�email �Ƿ�ռ��
	 */
	public boolean isRegisted(String email);
	
	/**
	 * ��֤��¼��Ϣ
	 */
	public User validateLoginInfo(String email, String md5);
	
	/**
	 * �����û���Ȩ(ֻ�ܸ��½�ɫ����) 
	 */
	public void updateAuthorize(User model, Integer[] ids);

	/**
	 * �����Ȩ 
	 */
	public void clearAuthorize(Integer userId);
	
}
