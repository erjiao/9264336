package com.erjiao.surveypark.struts2.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Question;
import com.erjiao.surveypark.service.SurveyService;

@Controller
@Scope("prototype")
public class QuestionAction extends BaseAction<Question> {

	private static final long serialVersionUID = 1L;
	
	private Integer sid;
	
	private Integer pid;
	
	private Integer qid;
	
	@Resource
	private SurveyService surveyService;
	
	
	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	/**
	 * 到达选择题型界面
	 */
	public String toSelectQuestionType() {
		return "selectQuestionTypePage";
	}
	
	/**
	 * 到达问题设计页面
	 */
	public String toDesignQuestionPage() {
		return "" + model.getQuestionType();
	}
	
	/**
	 * 保存/更新问题
	 */
	public String saveOrUpdateQuestion() {
		Page p = new Page();
		//维护关联关系
		p.setId(pid);
		model.setPage(p);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction";
	}
	
	/**
	 * 删除问题
	 */
	public String deleteQuestion() {
		surveyService.deleteQuestion(qid);
		return "designSurveyAction";
	}
	
	/**
	 * 编辑问题
	 */
	public String editQuestion() {
		this.model = surveyService.getQuestion(qid);
		return "" + model.getQuestionType();
	}
	

}
