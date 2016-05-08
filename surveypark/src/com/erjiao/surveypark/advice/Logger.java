package com.erjiao.surveypark.advice;

import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;

import com.erjiao.surveypark.model.Log;
import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;

public class Logger {
	
	@Resource
	private LogService logService;
	/**
	 * 记录 
	 */
	public Object record(ProceedingJoinPoint pjp) {
		Log log = new Log();
		try {
			ActionContext ac = ActionContext.getContext();
			//设置操作人
			if (null != ac) {
				Map<String, Object> session = ac.getSession();
				if (session != null) {
					User user = (User) session.get("user");
					if (user != null) {
						log.setOperator("" + user.getId() + ":" + user.getEmail());
					}
				}
			}
			//设置操作名称
			String mname = pjp.getSignature().getName();
			log.setOperName(mname);
			//设置操作参数
			Object[] params = pjp.getArgs();
			log.setOperParams(StringUtil.arr2str(params));
			//调用目标对象的方法
			Object ret = pjp.proceed();
			//设置操作结果
			log.setOperResult("success");
			//设置结果消息
			if (null != ret) {
				log.setResultMsg(ret.toString());
			}
			return ret;
		} catch (Throwable e) {
			log.setOperResult("failure");
			log.setResultMsg(e.getMessage());
			e.printStackTrace();
		} finally {
			logService.saveEntity(log);
		}
		return null;
	}
	
}
