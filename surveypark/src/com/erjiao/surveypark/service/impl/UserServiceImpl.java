package com.erjiao.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.model.security.Role;
import com.erjiao.surveypark.service.RoleService;
import com.erjiao.surveypark.service.UserService;
import com.erjiao.surveypark.util.ValidateUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService {
	
	//用于覆盖父类的方法
	/**
	 * 重写该方法, 目的是为了覆盖超类中该方法的注解, 指明注入指定的dao 对象, 否则spring
	 * 无法确定注入哪个Dao --- 有四个满足条件的Dao
	 */
	@Override
	@Resource(name="userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}
	
	@Resource
	private RoleService roleService;

	/**
	 * 判断 email 是否占用
	 */
	@Override
	public boolean isRegisted(String email) {
		String hql = "from User u where u.email = ?";
		List<User> list = this.findEntityByHQL(hql, email);
		return ValidateUtil.isValid(list);
	}

	@Override
	public User validateLoginInfo(String email, String md5) {
		String hql = "from User u where u.email = ? and u.password = ?";
		List<User> list = this.findEntityByHQL(hql, email, md5);
//		Connection connection = JdbcUtils.getConnection();
//		String sql = "select * from surveypark_answers";
//		try {
//			PreparedStatement psmt = connection.prepareStatement(sql);
//			ResultSet rs = psmt.executeQuery();
//			while (rs.next()) {
//				System.out.println(rs.getInt(1));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(connection);
		return ValidateUtil.isValid(list) ? list.get(0) : null;
	}

	@Override
	public void updateAuthorize(User model, Integer[] ids) {
		//查询新对象, 不可以对原有对象进行操纵
		User newUser = this.getEntity(model.getId());
		if (!ValidateUtil.isValid(ids)) {
			newUser.getRoles().clear();
		} else {
			List<Role> roles = roleService.findRolesInRange(ids);
			newUser.setRoles(new HashSet<Role>(roles));
			//不需要再调用更新方法, 因为是持久化对象.与当前session绑定, session关闭时, 自动提交.
		}
	}

	@Override
	public void clearAuthorize(Integer userId) {
		this.getEntity(userId).getRoles().clear();
	}
}
