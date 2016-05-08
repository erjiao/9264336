package com.erjiao.surveypark.service;

import java.util.List;

import com.erjiao.surveypark.model.Log;

public interface LogService extends BaseService<Log>{

	/**
	 * 通过表名创建日志表 
	 */
	void createLogTable(String tableName);
	
	/**
	 * 查询最近指定月份数的日志 
	 */
	List<Log> findNearestLogs(int i);
}
