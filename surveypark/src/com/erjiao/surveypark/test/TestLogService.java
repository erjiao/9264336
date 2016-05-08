package com.erjiao.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.service.SurveyService;

/**
 * ≤‚ ‘UserService
 * @author erjiao
 *
 */
public class TestLogService {
	
	private static LogService ls;
	
	@BeforeClass
	public static void initUserService(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		ls = (LogService) ac.getBean("logService");
	}
	
	@Test
	public void test0() {
		ls.findNearestLogs(3);
	}
	
}
