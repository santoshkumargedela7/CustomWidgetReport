package com.widget.CustomWidgetReport.service;

import java.util.List;

import com.widget.CustomWidgetReport.entity.User;

public interface UserService {

	User save(User user);

	boolean existsByUserNameIgnoreCase(String userName);

	List<User> findAll();

	User findById(Integer userId);

	void deleteById(Integer userId);

	User findByUserNameIgnoreCaseAndStatusIgnoreCase(String userName, String status);



}
