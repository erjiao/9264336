package com.erjiao.surveypark.service;

import java.util.List;

public interface BaseService<T> {

	//д����
	public void saveEntity(T t);
	public void saveOrUpdate(T t);
	public void updateEntity(T t);
	public void deleteEntity(T t);
	public void batchEntityByHQL(String hql, Object...args);
	//ִ��ԭ����sql ���
	public void executeSQL(String sql, Object ... args);
	
	//������
	public T loadEntity(Integer id);
	public T getEntity(Integer id);
	public List<T> findEntityByHQL(String hql, Object...args);
	//��ֵ����, ȷ����ѯ�������ֻ��һ����¼
	public Object uniqueResult(String hql, Object...args);
	
	//��ѯ����ʵ��
	public List<T> findAllEntities();
	
	//ִ��ԭ����sql��ѯ
	public List<?> executeSQLQuery(Class<?> clazz, String sql, Object ... args);
}
