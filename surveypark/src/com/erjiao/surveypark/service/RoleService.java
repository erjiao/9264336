package com.erjiao.surveypark.service;

import java.util.List;
import java.util.Set;

import com.erjiao.surveypark.model.security.Role;

/**
 * RoleService
 */
public interface RoleService extends BaseService<Role>{
	
	/**
	 * ����/���½�ɫ 
	 */
	void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * ��ѯ���ڽ�ɫ��Χ�ڵĽ�ɫ
	 */
	List<Role> findRolesNotInRange(Set<Role> roles);

	/**
	 * ��ѯ����ָ����Χ�ڵĽ�ɫ����
	 */
	List<Role> findRolesInRange(Integer[] ids);
	
}
