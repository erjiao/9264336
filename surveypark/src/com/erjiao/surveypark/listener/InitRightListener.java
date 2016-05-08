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
 * 初始化权限监听器, 在spring 容器初始化完成后立即调用 
 */
@Component
@SuppressWarnings("rawtypes")
public class InitRightListener implements ApplicationListener, ServletContextAware {
	
	@Resource
	private RightService rs;
	
	//接收ServletContext
	private ServletContext sc;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		//上下文刷新事件
		if (arg0 instanceof ContextRefreshedEvent) {
			//查出所有权限
			List<Right> rights = rs.findAllEntities();
			Map<String, Right> map = new HashMap<String, Right>();
			for (Right r : rights) {
				map.put(r.getRightUrl(), r);
			}
			if (null != sc) {
				sc.setAttribute("all_rights_map", map);
				System.out.println("初始化所有权限到Appication中.....");
			}
		}
	}

	//注入servletContext
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
		
	}

}
