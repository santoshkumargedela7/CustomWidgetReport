package com.widget.CustomWidgetReport.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.widget.CustomWidgetReport.entity.User;
import com.widget.CustomWidgetReport.repository.UserRepository;
import com.widget.CustomWidgetReport.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired 
	UserRepository userRepository;

	@Override
	public User save(User user) {
		
		try {
			User savedUser = userRepository.save(user);
			
			return savedUser;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public boolean existsByUserNameIgnoreCase(String userName) {
		// TODO Auto-generated method stub
		return userRepository.existsByUserNameIgnoreCase(userName);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User findById(Integer userId) {
		// TODO Auto-generated method stub
		
		try {
			User user = userRepository.findById(userId).get();
			return user;
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public void deleteById(Integer userId) {
		userRepository.deleteById(userId);
		
	}

	@Override
	public User findByUserNameIgnoreCaseAndStatusIgnoreCase(String userName, String status) {
		
		return userRepository.findByUserNameIgnoreCaseAndStatusIgnoreCase(userName, status);
	}

	
	
	
	
}