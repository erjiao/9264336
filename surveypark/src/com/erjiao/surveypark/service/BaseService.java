package com.erjiao.surveypark.service;

import java.util.List;

public interface BaseService<T> {

	//写操作
	public void saveEntity(T t);
	public void saveOrUpdate(T t);
	public void updateEntity(T t);
	public void deleteEntity(T t);
	public void batchEntityByHQL(String hql, Object...args);
	//执行原生的sql 语句
	public void executeSQL(String sql, Object ... args);
	
	//读操作
	public T loadEntity(Integer id);
	public T getEntity(Integer id);
	public List<T> findEntityByHQL(String hql, Object...args);
	//单值检索, 确保查询结果有且只有一条记录
	public Object uniqueResult(String hql, Object...args);
	
	//查询所有实体
	public List<T> findAllEntities();
	
	//执行原生的sql查询
	public List<?> executeSQLQuery(Class<?> clazz, String sql, Object ... args);
}
