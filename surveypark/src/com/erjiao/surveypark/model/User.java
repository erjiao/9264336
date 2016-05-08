package com.erjiao.surveypark.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.model.security.Role;


/**
 * �û���
 * @author erjiao
 *
 */
public class User extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String password;
	private String email;
	private String nickName;
	//ע��ʱ��
	private Date regDate = new Date();
	
	//Ȩ���ܺ�
	private long[] rightSum;
	
	//�Ƿ��ǳ�������Ա
	private boolean superAdmin;
	
	public boolean isSuperAdmin() {
		return superAdmin;
	}
	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	public long[] getRightSum() {
		return rightSum;
	}
	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}
	//��ɫ����
	private Set<Role> roles = new HashSet<Role>();
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	//������Survey �� User ֮����һ������ϵ
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	/**
	 * �����û���Ȩ���ܺ�
	 */
	public void calculateRightSum() {
		int pos = 0;
		long code = 0;
		for (Role role : roles) {
			//�ж��Ƿ񳬼�����Ա
			if ("-1".equals(role.getRoleValue())) {
				this.superAdmin = true;
				
				//�ͷ���Դ
				roles = null;
				return;
			}
			for (Right right : role.getRights()) {
				pos = right.getRightPos();
				code = right.getRightCode();
				rightSum[pos] = rightSum[pos] | code;
			}
		}
		//�ͷ���Դ
		roles = null;
	}
	
	/**
	 * �ж��û��Ƿ���ָ��Ȩ��
	 */
	public boolean hasRight(Right r) {
		int pos = r.getRightPos();
		long code = r.getRightCode();
		return !((rightSum[pos] & code) == 0);
	}
	
}
