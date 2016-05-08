package com.erjiao.surveypark.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.util.LogUtil;

/**
 * ������
 * 
 * ������־���ʯӢ����
 */
public class CreateLogTablesTask extends QuartzJobBean{
	
	private LogService logService;
	
	//ע��LogService
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	/**
	 * ������־��
	 */
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		//��һ����
		String tableName = LogUtil.generateLogTableName(1);
		logService.createLogTable(tableName);
		//��������
		tableName = LogUtil.generateLogTableName(2);
		logService.createLogTable(tableName);
		//��������
		tableName = LogUtil.generateLogTableName(3);
		logService.createLogTable(tableName);
	}

}
