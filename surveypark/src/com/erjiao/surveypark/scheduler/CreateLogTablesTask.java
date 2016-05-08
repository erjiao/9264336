package com.erjiao.surveypark.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.util.LogUtil;

/**
 * 调度器
 * 
 * 创建日志表的石英任务
 */
public class CreateLogTablesTask extends QuartzJobBean{
	
	private LogService logService;
	
	//注入LogService
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	/**
	 * 生成日志表
	 */
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		//下一个月
		String tableName = LogUtil.generateLogTableName(1);
		logService.createLogTable(tableName);
		//下两个月
		tableName = LogUtil.generateLogTableName(2);
		logService.createLogTable(tableName);
		//下三个月
		tableName = LogUtil.generateLogTableName(3);
		logService.createLogTable(tableName);
	}

}
