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
	 * ��¼ 
	 */
	public Object record(ProceedingJoinPoint pjp) {
		Log log = new Log();
		try {
			ActionContext ac = ActionContext.getContext();
			//���ò�����
			if (null != ac) {
				Map<String, Object> session = ac.getSession();
				if (session != null) {
					User user = (User) session.get("user");
					if (user != null) {
						log.setOperator("" + user.getId() + ":" + user.getEmail());
					}
				}
			}
			//���ò�������
			String mname = pjp.getSignature().getName();
			log.setOperName(mname);
			//���ò�������
			Object[] params = pjp.getArgs();
			log.setOperParams(StringUtil.arr2str(params));
			//����Ŀ�����ķ���
			Object ret = pjp.proceed();
			//���ò������
			log.setOperResult("success");
			//���ý����Ϣ
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
