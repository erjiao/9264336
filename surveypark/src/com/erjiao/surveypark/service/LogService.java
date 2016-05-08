package com.erjiao.surveypark.service;

import java.util.List;

import com.erjiao.surveypark.model.Log;

public interface LogService extends BaseService<Log>{

	/**
	 * ͨ������������־�� 
	 */
	void createLogTable(String tableName);
	
	/**
	 * ��ѯ���ָ���·�������־ 
	 */
	List<Log> findNearestLogs(int i);
}
