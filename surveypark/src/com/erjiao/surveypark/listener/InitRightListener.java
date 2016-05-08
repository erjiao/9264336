package com.erjiao.surveypark.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.service.RightService;

/**
 * ��ʼ��Ȩ�޼�����, ��spring ������ʼ����ɺ��������� 
 */
@Component
@SuppressWarnings("rawtypes")
public class InitRightListener implements ApplicationListener, ServletContextAware {
	
	@Resource
	private RightService rs;
	
	//����ServletContext
	private ServletContext sc;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		//������ˢ���¼�
		if (arg0 instanceof ContextRefreshedEvent) {
			//�������Ȩ��
			List<Right> rights = rs.findAllEntities();
			Map<String, Right> map = new HashMap<String, Right>();
			for (Right r : rights) {
				map.put(r.getRightUrl(), r);
			}
			if (null != sc) {
				sc.setAttribute("all_rights_map", map);
				System.out.println("��ʼ������Ȩ�޵�Appication��.....");
			}
		}
	}

	//ע��servletContext
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
		
	}

}