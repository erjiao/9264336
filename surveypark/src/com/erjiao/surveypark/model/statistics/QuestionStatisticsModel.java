package com.erjiao.surveypark.model.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.erjiao.surveypark.model.Question;

/**
 * 问题统计模型 
 */
public class QuestionStatisticsModel implements Serializable{
	
	private static final long serialVersionUID = -7379157122842289926L;
	private Question question; //
	private int count; //回答该问题的人数
	
	//选项统计模型的集合
	private List<OptionStatisticsModel> osms = new ArrayList<OptionStatisticsModel>();
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<OptionStatisticsModel> getOsms() {
		return osms;
	}
	public void setOsms(List<OptionStatisticsModel> osms) {
		this.osms = osms;
	}
	
}
