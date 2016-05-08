package com.erjiao.surveypark.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.erjiao.surveypark.dao.BaseDao;

/**
 * �����dao ʵ��, ר�����ڼ���
 * @author erjiao
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	
	//ע��SessionFactory
	@Resource
	private SessionFactory sf;
	
	private Class<T> clazz;
	
	public BaseDaoImpl(){
		//�õ�����
		Type superType = this.getClass().getGenericSuperclass();
		//��������Ƿ�����
		if (superType instanceof ParameterizedType) {
			//��ֱ��ǿתΪ���ͻ�����
			ParameterizedType type = (ParameterizedType) superType;
			//�õ����ͻ������з��Ͳ�����ʵ����������
			Type[] typeArgs = type.getActualTypeArguments();
			
			if (typeArgs != null && typeArgs.length > 0) {
				clazz = (Class<T>) typeArgs[0];
			}
		}
		
	}
	
	public void saveEntity(T t) {
		sf.getCurrentSession().save(t);
	}

	public void saveOrUpdate(T t) {
		sf.getCurrentSession().saveOrUpdate(t);
	}

	public void updateEntity(T t) {
		sf.getCurrentSession().update(t);
	}

	public void deleteEntity(T t) {
		sf.getCurrentSession().delete(t);
	}
	
	/**
	 * ����HQL ��� ʵ����������
	 */
	public void batchEntityByHQL(String hql, Object... args) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		query.executeUpdate();
		
	}
	
	//��get����: 1.�д��� 2.�����ݿ��޼�¼, ���׳��쳣. �� get �򷵻� null.
	public T loadEntity(Integer id) {
		return (T) sf.getCurrentSession().load(clazz, id);
	}

	public T getEntity(Integer id) {
		return (T) sf.getCurrentSession().get(clazz, id);
	}
	
	
	public List<T> findEntityByHQL(String hql, Object... args) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return query.list();
	}
	
	//��ֵ����, ȷ����ѯ�������ֻ��һ����¼
	public Object uniqueResult(String hql, Object... args) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return query.uniqueResult();
	}

	@Override
	public void executeSQL(String sql, Object... args) {
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < args.length; i ++) {
			q.setParameter(i, args[i]);
		}
		q.executeUpdate();
	}

	@Override
	public List<?> executeSQLQuery(Class<?> clazz, String sql, Object... args) {
		SQLQuery q = sf.getCurrentSession().createSQLQuery(sql);
		if (clazz != null) {
			//ʹ�ò�ѯ���ݿ��Է�װ�ɶ���, ����ֻ�� Object����
			q.addEntity(clazz);
		}
		for (int i = 0; i < args.length; i ++) {
			q.setParameter(i, args[i]);
		}
		return q.list();
	}
}
