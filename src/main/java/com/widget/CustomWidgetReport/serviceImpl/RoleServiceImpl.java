package com.widget.CustomWidgetReport.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.widget.CustomWidgetReport.entity.Role;
import com.widget.CustomWidgetReport.repository.RoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements com.widget.CustomWidgetReport.service.RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> getRolesByStatus(String status) {
		return roleRepository.findAllByStatusIgnoreCase(status);
	}

	@Override
	public void deleteById(Integer roleId) {
		roleRepository.deleteById(roleId);

	}

	@Override
	public Role findById(Integer roleId) {
		return roleRepository.findById(roleId).get();
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public boolean existsByRoleNameIgnoreCase(String roleName) {
		return roleRepository.existsByRoleNameIgnoreCase(roleName);
	}

	@Override
	public List<Role> getAllRoles() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

}
