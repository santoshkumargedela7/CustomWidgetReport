package com.widget.CustomWidgetReport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.widget.CustomWidgetReport.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{

	List<Role> findAllByStatusIgnoreCase(String status);

	boolean existsByRoleNameIgnoreCase(String roleName);

}
