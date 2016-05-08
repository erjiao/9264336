package com.erjiao.surveypark.struts2.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 抽象action, 专门用于继承.
 */
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>,
		Preparable {
	
	private static final long serialVersionUID = 1L;

	public T model;
	
	@SuppressWarnings("unchecked")
	public BaseAction() {
		Type superType = this.getClass().getGenericSuperclass();
		if (superType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) superType;
			Type[] typeArgs = type.getActualTypeArguments();
			if (typeArgs != null && typeArgs.length > 0) {
				Class<?> clazz = (Class<?>) typeArgs[0];
				try {
					model = (T) clazz.newInstance();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
	}
	
	
	@Override
	public void prepare() throws Exception {

	}

	@Override
	public T getModel() {
		return model;
	}

}
