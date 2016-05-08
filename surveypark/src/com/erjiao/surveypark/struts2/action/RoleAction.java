package com.erjiao.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.model.security.Role;
import com.erjiao.surveypark.service.RightService;
import com.erjiao.surveypark.service.RoleService;

/**
 * @author erjiao
 *
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = 1L;
	
	@Resource
	private RoleService roleService;
	
	@Resource
	private RightService rightSerivce;
	
	private List<Role> allRoles;

	private List<Right> noOwnRights;
	
	private Integer roleId;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	//��ɫӵ�е�Ȩ��id ����
	private Integer[] ownRightIds;

	private Integer userId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}

	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}

	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}

	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}
	
	/**
	 * ��ѯ���н�ɫ 
	 */
	public String findAllRoles() {
		this.allRoles = roleService.findAllEntities();
		return "roleListPage";
	}
	
	/**
	 * ���Ȩ��
	 */
	public String toAddRolePage() {
		this.noOwnRights = rightSerivce.findAllEntities();
		return "addRolePage";
	}
	
	/**
	 * ������½�ɫ
	 */
	public String saveOrUpdateRole() {
		roleService.saveOrUpdateRole(model, ownRightIds);
		return "findAllRolesAction";
	}
	
	/**
	 * �༭��ɫ
	 */
	public String editRole() {
		this.model = roleService.getEntity(roleId);
		this.noOwnRights = rightSerivce.findRightsNotInRange(model.getRights());
		return "editRolePage";
	}
	
	/**
	 * ɾ����ɫ
	 */
	public String deleteRole() {
		Role r = new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction";
	}
}
