package com.erjiao.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.service.RightService;

@Controller
@Scope("prototype")
public class RightAction extends BaseAction<Right> {

	private static final long serialVersionUID = 1L;
	
	private List<Right> allRights;
	
	private Integer rightId;

	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public List<Right> getAllRights() {
		return allRights;
	}

	public void setAllRights(List<Right> allRights) {
		this.allRights = allRights;
	}

	@Resource
	private RightService rightService;
	
	/**
	 * ��ѯ����Ȩ�� 
	 */
	public String findAllRights() {
		this.allRights = rightService.findAllEntities();
		System.out.println("------------------------");
		return "rightListPage";
	}
	
	/**
	 * ���Ȩ��
	 */
	public String toAddRightPage() {
		return "addRigthPage";
	}
	
	/**
	 * ��������Ȩ��
	 */
	public String saveOrUpdateRight() {
		rightService.saveOrUpdateRight(model);
		return "findAllRightsAction";
	}
	
	/**
	 * �༭Ȩ�� 
	 */
	public String editRight() {
		this.model = rightService.getEntity(rightId);
		return "editRightPage";
	}
	
	/**
	 * ɾ��Ȩ�� 
	 */
	public String deleteRight() {
		Right r = new Right();
		r.setId(rightId);
		rightService.deleteEntity(r);
		return "findAllRightsAction";
	}
	
	/**
	 * ��������Ȩ��
	 */
	public String batchUpdateRights() {
		rightService.batchUpdateRigths(allRights);
		return "findAllRightsAction";
	}

}
