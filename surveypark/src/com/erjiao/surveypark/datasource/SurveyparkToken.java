package com.erjiao.surveypark.datasource;

import com.erjiao.surveypark.model.Survey;

/**
 * ����
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
	 * ��ָ�������ƶ���󶨵���ǰ�߳� 
	 */
	public static void bindToken(SurveyparkToken token) {
		l.set(token);
	}
	
	/**
	 * �����ǰ�̰߳󶨵�����
	 */
	public static void unBindToken() {
		l.remove();
	}
	
	/**
	 * ��ȡ��ǰ�̰߳󶨵����� 
	 */
	public static SurveyparkToken getToken() {
		return l.get();
	}
	
}
