package com.erjiao.surveypark.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.service.BaseService;

/**
 * 抽象的BaseService, 专门用于继承.
 * @author erjiao
 *
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
	private BaseDao<T> dao;
	
	private Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type =  (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	//注入dao(会报错, 容器中有4个dao(pageDao, surveyDao, userDao, questionDao), 未明确指定注入哪一个dao)
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveEntity(T t) {
		dao.saveEntity(t);
	}

	@Override
	public void saveOrUpdate(T t) {
		dao.saveOrUpdate(t);
	}

	@Override
	public void updateEntity(T t) {
		dao.updateEntity(t);
	}

	@Override
	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}

	@Override
	public void batchEntityByHQL(String hql, Object... args) {
		dao.batchEntityByHQL(hql, args);
	}

	@Override
	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}

	@Override
	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}

	@Override
	public List<T> findEntityByHQL(String hql, Object... args) {
		return dao.findEntityByHQL(hql, args);
	}

	@Override
	public Object uniqueResult(String hql, Object... args) {
		return dao.uniqueResult(hql, args);
	}

	@Override
	public List<T> findAllEntities() {
		String hql = "from " + clazz.getSimpleName();
		return this.findEntityByHQL(hql);
	}

	@Override
	public void executeSQL(String sql, Object... args) {
		dao.executeSQL(sql, args);
	}

	@Override
	public List<?> executeSQLQuery(Class<?> clazz, String sql, Object... args) {
		return dao.executeSQLQuery(clazz, sql, args);
	}
	
}
