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
 * 抽象的dao 实现, 专门用于集成
 * @author erjiao
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	
	//注入SessionFactory
	@Resource
	private SessionFactory sf;
	
	private Class<T> clazz;
	
	public BaseDaoImpl(){
		//得到父类
		Type superType = this.getClass().getGenericSuperclass();
		//如果父类是泛型类
		if (superType instanceof ParameterizedType) {
			//则直接强转为泛型化超类
			ParameterizedType type = (ParameterizedType) superType;
			//得到泛型化超类中泛型参数的实际类型数组
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
	 * 按照HQL 语句 实现批量更新
	 */
	public void batchEntityByHQL(String hql, Object... args) {
		Query query = sf.getCurrentSession().createQuery(hql);
		for (int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		query.executeUpdate();
		
	}
	
	//与get区别: 1.有代理 2.若数据库无记录, 则抛出异常. 而 get 则返回 null.
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
	
	//单值检索, 确保查询结果有且只有一条记录
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
			//使得查询数据可以封装成对象, 否则只是 Object数组
			q.addEntity(clazz);
		}
		for (int i = 0; i < args.length; i ++) {
			q.setParameter(i, args[i]);
		}
		return q.list();
	}
}
