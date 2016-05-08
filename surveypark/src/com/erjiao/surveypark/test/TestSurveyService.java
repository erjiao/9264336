package com.erjiao.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.SurveyService;
import com.erjiao.surveypark.service.UserService;

/**
 * ≤‚ ‘UserService
 * @author erjiao
 *
 */
public class TestSurveyService {
	
	private static SurveyService ss;
	
	@BeforeClass
	public static void initUserService(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		ss = (SurveyService) ac.getBean("surveyService");
	}
	
	@Test
	public void testToggleStatus() {
		ss.toggleStatus(4);
	}
	
	@Test
	public void getSurvey() {
		ss.getSurvey(2);
	}
	
}
