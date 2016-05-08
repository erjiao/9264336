package com.erjiao.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.Log;
import com.erjiao.surveypark.service.LogService;

@Controller
@Scope("prototype")
public class LogAction extends BaseAction<Log> {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private LogService logService;

	private List<Log> logs;
	
	//默认查询日志的月份数
	private int monthNum = 2;
	
	public int getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	/**
	 * 查询全部日志 
	 */
	public String findAllLogs() {
		this.logs = logService.findAllEntities();
		return "logListPage";
	}
	
	/**
	 * 查询最近的日志
	 */
	public String findNearestLogs() {
		this.logs = logService.findNearestLogs(monthNum);
		return "logListPage";
	}
}
