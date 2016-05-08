package com.erjiao.surveypark.struts2.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.model.security.Role;
import com.erjiao.surveypark.service.RoleService;
import com.erjiao.surveypark.service.UserService;

/**
 *  用户授权action
 */
@Controller
@Scope("prototype")
public class UserAuthorizeAction extends BaseAction<User> {

	private static final long serialVersionUID = 1L;
	
	private List<User> allUsers;
	
	private Integer userId;
	
	private List<Role> noOwnRoles;
	
	private Integer[] ownRoleIds;
	
	public Integer[] getOwnRoleIds() {
		return ownRoleIds;
	}

	public void setOwnRoleIds(Integer[] ownRoleIds) {
		this.ownRoleIds = ownRoleIds;
	}

	public List<Role> getNoOwnRoles() {
		return noOwnRoles;
	}

	public void setNoOwnRoles(List<Role> noOwnRoles) {
		this.noOwnRoles = noOwnRoles;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	
	public String findAllUsers() {
		this.allUsers = userService.findAllEntities();
		return "userAuthorizeListPage";
	} 
	
	/**
	 * 修改授权
	 * @return
	 */
	public String editAuthorize() {
		this.model = userService.getEntity(userId);
		this.noOwnRoles = roleService.findRolesNotInRange(model.getRoles());
		return "userAuthorizePage";
	}
	
	/**
	 * 更新授权
	 */
	public String updateAuthorize() {
		userService.updateAuthorize(model, ownRoleIds);
		return "findAllUsersAction";
	}
	
	public String clearAuthorize() {
		userService.clearAuthorize(userId);
		return "findAllUsersAction";
	}
	
}
