package com.erjiao.surveypark.service;

import java.util.List;
import java.util.Set;

import com.erjiao.surveypark.model.security.Role;

/**
 * RoleService
 */
public interface RoleService extends BaseService<Role>{
	
	/**
	 * 保存/更新角色 
	 */
	void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * 查询不在角色范围内的角色
	 */
	List<Role> findRolesNotInRange(Set<Role> roles);

	/**
	 * 查询不在指定范围内的角色集合
	 */
	List<Role> findRolesInRange(Integer[] ids);
	
}
