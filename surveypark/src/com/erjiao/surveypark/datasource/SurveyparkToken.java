package com.erjiao.surveypark.datasource;

import com.erjiao.surveypark.model.Survey;

/**
 * 令牌
 */
public class SurveyparkToken {
	
	private static ThreadLocal<SurveyparkToken> l = new ThreadLocal<SurveyparkToken>();
	
	private Survey survey;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	
	/**
	 * 将指定的令牌对象绑定到当前线程 
	 */
	public static void bindToken(SurveyparkToken token) {
		l.set(token);
	}
	
	/**
	 * 解除当前线程绑定的令牌
	 */
	public static void unBindToken() {
		l.remove();
	}
	
	/**
	 * 获取当前线程绑定的令牌 
	 */
	public static SurveyparkToken getToken() {
		return l.get();
	}
	
}
