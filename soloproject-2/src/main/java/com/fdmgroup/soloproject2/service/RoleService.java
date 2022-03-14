package com.fdmgroup.soloproject2.service;

import com.fdmgroup.soloproject2.model.Role;

public interface RoleService {
	
	Role findByRoleName(String roleName);
}
