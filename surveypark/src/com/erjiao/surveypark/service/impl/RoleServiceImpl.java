package com.erjiao.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.model.security.Role;
import com.erjiao.surveypark.service.RightService;
import com.erjiao.surveypark.service.RoleService;
import com.erjiao.surveypark.util.DataUtil;
import com.erjiao.surveypark.util.StringUtil;
import com.erjiao.surveypark.util.ValidateUtil;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService {
	
	@Resource
	private RightService rightService;
	
	@Resource(name="roleDao")
	public void setDao(BaseDao<Role> dao) {
		super.setDao(dao);
	}

	@Override
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds) {
		//没有给角色授予任何权限
		if (!ValidateUtil.isValid(ownRightIds)) {
			model.getRights().clear();
		} else {
			List<Right> rights = rightService.findRightsInRange(ownRightIds);
			model.setRights(new HashSet<Right>(rights));
		}
		this.saveOrUpdate(model);
	}

	@Override
	public List<Role> findRolesNotInRange(Set<Role> roles) {
		if (!ValidateUtil.isValid(roles)) {
			return this.findAllEntities();
		} else {
			String hql = "from Role r where r.id not in (" + DataUtil.extractEntityIds(roles)+ ")";
			return this.findEntityByHQL(hql);
		}
	}

	@Override
	public List<Role> findRolesInRange(Integer[] ids) {
		if (ValidateUtil.isValid(ids)) {
			String hql = "from Role r where r.id in (" + StringUtil.arr2str(ids) + ")";
			return this.findEntityByHQL(hql);
		}
		return null;
	}
	
}
