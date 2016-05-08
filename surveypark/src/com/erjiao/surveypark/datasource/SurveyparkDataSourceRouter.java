package com.erjiao.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义数据源路由器(分布式数据库) 
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource {
	
	/**
	 * 检测当前key
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		SurveyparkToken token = SurveyparkToken.getToken();
		if (token != null) {
			Integer id = token.getSurvey().getId();
			//解除令牌的绑定
			SurveyparkToken.unBindToken();
			return (id % 2 == 0) ? "even" : "odd";
		}
		return null;
	}

}
