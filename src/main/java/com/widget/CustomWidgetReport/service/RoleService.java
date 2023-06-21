package com.widget.CustomWidgetReport.service;

import java.util.List;

import com.widget.CustomWidgetReport.entity.Role;

public interface RoleService {
	
	List<Role> getRolesByStatus(String status);
	
	void deleteById(Integer roleId);

	Role findById(Integer roleId);

	Role save(Role role);
	
	boolean existsByRoleNameIgnoreCase(String roleName);
	
	List<Role> findAll();
	
}
