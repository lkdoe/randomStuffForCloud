package com.fdmgroup.soloproject2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7244732672934012398L;
	
	@Id @GeneratedValue
	private Integer roleId;
	private String roleName;
	
	@Override
	public String getAuthority() { 
		return "ROLE_" + roleName; 
	}
	
	public Role() {}
	public Role(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleId() { return roleId; }

	public String getRoleName() { return roleName; }

	public void setRoleId(Integer roleId) { this.roleId = roleId; }

	public void setRoleName(String roleName) { this.roleName = roleName; }
	
	
}
