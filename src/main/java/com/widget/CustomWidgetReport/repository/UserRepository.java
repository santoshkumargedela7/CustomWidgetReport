package com.widget.CustomWidgetReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.widget.CustomWidgetReport.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUserNameIgnoreCaseAndStatusIgnoreCase(String userName, String status);

	boolean existsByUserNameIgnoreCase(String userName);

}
