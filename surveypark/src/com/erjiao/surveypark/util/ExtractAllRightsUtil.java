package com.erjiao.surveypark.util;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erjiao.surveypark.service.RightService;

/**
 * 提取所有权限工具类 
 */
public class ExtractAllRightsUtil {

	public static void main(String[] args) throws URISyntaxException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		RightService rs = (RightService) ac.getBean("rightService");
		ClassLoader loader = ExtractAllRightsUtil.class.getClassLoader();
		URL url = loader.getResource("com/erjiao/surveypark/struts2/action");
		
		File dir = new File(url.toURI());
		File[] files = dir.listFiles();
		String fname = "";
		for (File f : files) {
			fname = f.getName();
			if (fname.endsWith(".class") && 
					!fname.equals("BaseAction.class")) {
				//System.out.println(fname);
				processAction(fname, rs);
			}
		}
	}

	/**
	 * 处理action类, 捕获所有url 地址, 形成权限 
	 */
	private static void processAction(String fname, RightService rs) {
		String pkgName = "com.erjiao.surveypark.struts2.action";
		String simpleClassName = fname.substring(0, fname.indexOf(".class"));
		//System.out.println(simpleClassName);
		String className = pkgName + "." + simpleClassName;
		//得到具体的类
		try {
			Class<?> clazz = Class.forName(className);
			Method[] methods = clazz.getDeclaredMethods();
			Class<?> retType = null;
			String mname = null;
			Class[] paramType = null;
			String url = "";
			for (Method m : methods) {
				retType = m.getReturnType();
				mname = m.getName();
				paramType = m.getParameterTypes();
				if (retType == String.class 
						&& !ValidateUtil.isValid(paramType)
						&& Modifier.isPublic(m.getModifiers())) {
					if (mname.equals("execute")) {
						url = "/" + simpleClassName;
					} else {
						url = "/" + simpleClassName + "_" + mname;
					}
					rs.appendRightByURL(url);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
