package com.erjiao.surveypark.service;

import java.util.List;
import java.util.Set;

import com.erjiao.surveypark.model.security.Right;

/**
 * RightService
 */
public interface RightService extends BaseService<Right>{
	
	/**
	 * ����/����Ȩ�� 
	 */
	void saveOrUpdateRight(Right model);

	/**
	 * ����url׷��Ȩ��
	 */
	void appendRightByURL(String url);

	/**
	 * ��������Ȩ�� 
	 */
	void batchUpdateRigths(List<Right> allRights);
	
	/**
	 * ��ѯ��ָ����Χ�ڵ�Ȩ��
	 */
	List<Right> findRightsInRange(Integer[] ids);

	/**
	 * ��ѯ����ָ����Χ�ڵ�Ȩ��
	 */
	List<Right> findRightsNotInRange(Set<Right> rights);
	
	
	/**
	 * ��ѯ���Ȩ��λ
	 */
	int getMaxRightPos();
}
