package com.erjiao.surveypark.dao;

import java.util.List;

/**
 * BaseDao �ӿ�
 * @author erjiao
 *
 * @param <T>
 */
public interface BaseDao<T> {
	
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
	//ִ��ԭ����sql��ѯ(����ָ���Ƿ��װ��ʵ��)
	public List<?> executeSQLQuery(Class<?> clazz, String sql, Object ... args);
	
}
