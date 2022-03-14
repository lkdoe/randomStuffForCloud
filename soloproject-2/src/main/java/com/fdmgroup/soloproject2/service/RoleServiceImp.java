package com.fdmgroup.soloproject2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.soloproject2.model.Role;
import com.fdmgroup.soloproject2.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService {
	private RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImp(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName).orElse(null);
	}

}
