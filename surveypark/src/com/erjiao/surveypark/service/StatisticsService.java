package com.erjiao.surveypark.service;

import com.erjiao.surveypark.model.statistics.QuestionStatisticsModel;

/**
 * 统计服务 
 */
public interface StatisticsService {
	
	public QuestionStatisticsModel statistics(Integer qid);
	
}
