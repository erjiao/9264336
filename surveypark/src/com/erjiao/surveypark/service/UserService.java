package com.erjiao.surveypark.service;

import com.erjiao.surveypark.model.User;

/**
 * UserService
 */
public interface UserService extends BaseService<User>{
	
	/**
	 * 判断email 是否被占用
	 */
	public boolean isRegisted(String email);
	
	/**
	 * 验证登录信息
	 */
	public User validateLoginInfo(String email, String md5);
	
	/**
	 * 更新用户授权(只能更新角色设置) 
	 */
	public void updateAuthorize(User model, Integer[] ids);

	/**
	 * 清除授权 
	 */
	public void clearAuthorize(Integer userId);
	
}
