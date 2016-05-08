package com.erjiao.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * �Զ�������Դ·����(�ֲ�ʽ���ݿ�) 
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource {
	
	/**
	 * ��⵱ǰkey
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		SurveyparkToken token = SurveyparkToken.getToken();
		if (token != null) {
			Integer id = token.getSurvey().getId();
			//������Ƶİ�
			SurveyparkToken.unBindToken();
			return (id % 2 == 0) ? "even" : "odd";
		}
		return null;
	}

}
