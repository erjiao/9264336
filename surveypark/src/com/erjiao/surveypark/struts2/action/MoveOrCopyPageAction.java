package com.erjiao.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.SurveyService;
import com.erjiao.surveypark.struts2.UserAware;

/**
 * �ƶ�/��ֵҳ
 */
@Controller
@Scope("prototype")
public class MoveOrCopyPageAction extends BaseAction<Page> implements UserAware{

	private static final long serialVersionUID = 1L;
	
	//ԭҳ�� id
	private Integer srcPid;
	
	//Ŀ��ҳ��id
	private Integer targPid;
	
	//λ��: 0-֮ǰ, 1-֮��
	private int pos;
	
	//Ŀ����� id
	private Integer sid;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getTargPid() {
		return targPid;
	}

	public void setTargPid(Integer targPid) {
		this.targPid = targPid;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	private List<Survey> mySurveys;
	
	@Resource
	private SurveyService surveyService;

	private User user;
	
	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}

	public Integer getSrcPid() {
		return srcPid;
	}

	public void setSrcPid(Integer srcPid) {
		this.srcPid = srcPid;
	}

	/**
	 * �����ƶ�/��ֵҳ�б�ҳ��
	 */
	public String toSelectTargetPage() {
		this.mySurveys = surveyService.getSurveyWithPages(user);
		return "moveOrCopyPageListPage";
	}
	
	/**
	 * 	����ҳ���ƶ�/��ֵ
	 */
	public String doMoveOrCopyPage() {
		surveyService.moveOrCopyPage(srcPid, targPid, pos);
		return "designSurveyAction";
	}
	
	//ע��user
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	

}
