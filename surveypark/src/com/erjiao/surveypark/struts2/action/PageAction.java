package com.erjiao.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.service.SurveyService;

@Controller
@Scope("prototype")
public class PageAction extends BaseAction<Page> {

	private static final long serialVersionUID = 1L;
	
	private Integer sid;
	
	private Integer pid;
	
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	//ע�� surveyService
	@Resource
	private SurveyService surveyService;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	/**
	 * ��������page ��ҳ��
	 */
	public String toAddPage() {
		return "addPagePage";
	}
	
	/**
	 * ����/����ҳ��
	 */
	public String saveOrUpdatePage() {
		//ά��������ϵ
		Survey s = new Survey();
		s.setId(sid);
		model.setSurvey(s);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	
	/**
	 * �༭ҳ��
	 */
	public String editPage() {
		this.model = surveyService.getPage(pid);
		System.out.println(model);
		return "editPagePage";
	}
	
	/**
	 * ɾ��ҳ��
	 */
	public String deletePage() {
		surveyService.deletePage(pid);
		return "designSurveyAction";
	}
}