package com.erjiao.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.erjiao.surveypark.service.StatisticsService;

/**
 * ≤‚ ‘StatisticsService
 * @author erjiao
 *
 */
public class TestStatisticsService {
	
	private static StatisticsService ss;
	
	@BeforeClass
	public static void initUserService(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		ss = (StatisticsService) ac.getBean("statisticsService");
	}
	
	@Test
	public void statistics() {
		ss.statistics(6);
	}

	
}
