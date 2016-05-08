package com.erjiao.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.SurveyService;

/**
 * ≤‚ ‘UserService
 * @author erjiao
 *
 */
public class TestUserService {
	
	private static SurveyService ss;
	
	@BeforeClass
	public static void initUserService(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		ss = (SurveyService) ac.getBean("surveyService");
	}
	
	@Test
	public void newSurvey() {
		User u = new User();
		u.setId(1);
		ss.newSurvey(u);
	}
	
}
