package com.erjiao.surveypark.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * �����ʵ�峬��, ר�����ڼ̳� 
 */
public abstract class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	public abstract Integer getId();

	public abstract void setId(Integer id);

	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();
			Class clazz = this.getClass();
			String simpleName = clazz.getSimpleName();
			buffer.append(simpleName);
			buffer.append("{");
			Field[] fs = clazz.getDeclaredFields();
			Class ftype = null;
			String fname = null;
			Object fvalue = null;
			for (Field f : fs) {
				ftype = f.getType();
				fname = f.getName();
				f.setAccessible(true);
				fvalue = f.get(this);
				//�Ƿ��ǻ�����������
				if ((ftype.isPrimitive()
						|| ftype == Integer.class
						|| ftype == Long.class
						|| ftype == Short.class
						|| ftype == Boolean.class
						|| ftype == Character.class
						|| ftype == Double.class
						|| ftype == Float.class
						|| ftype == String.class)
						&& !Modifier.isStatic(ftype.getModifiers())
						&& !"serialVersionUID".equals(fname)) {
					buffer.append(fname);
					buffer.append(":");
					buffer.append(fvalue + ",");
				}
			}
			buffer.deleteCharAt(buffer.length() - 1).append("}");
			return buffer.toString();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
