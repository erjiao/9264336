package com.erjiao.surveypark.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.util.LogUtil;

/**
 * 初始化日志表监听器
 */
@Component
@SuppressWarnings("rawtypes")
public class IniLogTablesListener implements ApplicationListener {
	
	@Resource
	private LogService logService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		//上下文刷新事件
		if (arg0 instanceof ContextRefreshedEvent) {
			String tableName = LogUtil.generateLogTableName(0);
			logService.createLogTable(tableName);
			
			tableName = LogUtil.generateLogTableName(1);
			logService.createLogTable(tableName);
			
			tableName = LogUtil.generateLogTableName(2);
			logService.createLogTable(tableName);
		}
	}
}
