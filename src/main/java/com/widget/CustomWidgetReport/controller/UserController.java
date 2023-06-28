package com.widget.CustomWidgetReport.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.widget.CustomWidgetReport.dto.UserDto;
import com.widget.CustomWidgetReport.entity.Feature;
import com.widget.CustomWidgetReport.entity.Role;
import com.widget.CustomWidgetReport.entity.User;
import com.widget.CustomWidgetReport.util.Aes;
import com.widget.CustomWidgetReport.util.CustomWidgetLog;
import com.widget.CustomWidgetReport.util.EntityConstants.Status;
import com.widget.CustomWidgetReport.util.Response;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private static final Logger log = LogManager.getLogger("UserController"); 
	
	@Autowired
	private com.widget.CustomWidgetReport.service.UserService userService;

	@Autowired
	private com.widget.CustomWidgetReport.repository.RoleRepository roleRepository;

	@Autowired
	private Aes aes;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/save")
	public Response<User> saveUser(@RequestBody UserDto userDto) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		User user = modelMapper.map(userDto, User.class);
		if (userService.existsByUserNameIgnoreCase(userDto.getUserName())) {
			return new Response<User>(400, false, HttpStatus.BAD_REQUEST, "Username already In Use").setResponse(null);
		}
		try {
			if (userDto != null) {

				Role role = roleRepository.findById(userDto.getRoleId()).get();
				user.setRole(role);
				user.setPassword(aes.encrypt(userDto.getPassword()));
				user.setCreatedOn(LocalDateTime.now().format(format));
				user = userService.save(user);

				log.info(CustomWidgetLog.getCurrentClassAndMethodName() + user);
			}
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while creating user : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
			return new Response<User>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating user")
					.setResponse(null);
		}
		return new Response<User>(200, true, HttpStatus.OK, "User has been created").setResponse(user);

	}

	@GetMapping("/list")
	public Response<List<User>> getUsers() {
		List<User> users = new ArrayList<>();
		try {
			users = userService.findAll();
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + users);
			for (User u : users) {
				u.setPassword(aes.decrypt(u.getPassword()));
			}
			users.removeIf(
					u -> (u.getUserName().equalsIgnoreCase("superadmin") || u.getStatus().equalsIgnoreCase("D")));
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while listing user : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
			return new Response<List<User>>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting users")
					.setResponse(null);
		}
		return new Response<List<User>>(200, true, HttpStatus.OK, users.size() + " users has been retrived")
				.setResponse(users);
	}

	@PutMapping("/update/{userId}")
	public Response<User> updateUser(@PathVariable Integer userId, @RequestBody UserDto userDto) {
		log.info("userDto : " + userDto);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		User user = modelMapper.map(userDto, User.class);

		try {
			if (userDto != null) {
				user = userService.findById(userId);
				log.info(CustomWidgetLog.getCurrentClassAndMethodName() + user);
				if (user != null) {

					Role role = roleRepository.findById(userDto.getRoleId()).get();
					user.setRole(role);
					user.setUpdatedOn(LocalDateTime.now().format(format));
					user = userService.save(user);
					log.info(CustomWidgetLog.getCurrentClassAndMethodName() + user);
				} else {
					return new Response<User>(404, false, HttpStatus.NOT_FOUND, "User not found").setResponse(user);
				}
			}
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while updating user : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
			return new Response<User>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating user")
					.setResponse(null);
		}
		return new Response<User>(200, true, HttpStatus.OK, "User has been updated").setResponse(user);

	}

	@DeleteMapping("/delete/{userId}")
	public Response<String> deleteUserById(@PathVariable Integer userId) {
		try {

			try {
				User user = userService.findById(userId);
				log.info(CustomWidgetLog.getCurrentClassAndMethodName() + "user got deleted--->" + user);
			} catch (Exception e) {
				return new Response<String>(500, false, HttpStatus.NOT_FOUND,
						"No user found with the userId : " + userId).setResponse(null);
			}
			userService.deleteById(userId);
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while deleting user by id : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack error : "
					+ Arrays.toString(e.getStackTrace()));
			return new Response<String>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting user")
					.setResponse(null);
		}
		return new Response<String>(200, true, HttpStatus.OK, "User has been deleted").setResponse(null);
	}

	@GetMapping("/hasAccess/{userId}")
	public List<String> userFeatures(@PathVariable Integer userId) {
		List<String> features = new ArrayList<>();
		Set<Feature> userfeatures = new HashSet<>();

		User user = null;
		try {
			user = userService.findById(userId);
			userfeatures = user.getRole().getFeatures();
			for (Feature f : userfeatures) {
				features.add(f.getFeatureName());
			}
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while getting user features : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
		}
		return features;
	}

	@GetMapping("/supportDtls/{userId}")
	public String[] supportDtls(@PathVariable Integer userId) {
		User user = null;
		String usersupport = null;
		try {
			user = userService.findById(userId);
			usersupport = user.getRole().getSupportType();
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while getting user features : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
		}
		return usersupport.split(",");

	}

	@GetMapping("/findUser/{userName}")
	public Response<User> findByUserName(@PathVariable String userName) {

		User user = null;
		try {
			user = userService.findByUserNameIgnoreCaseAndStatusIgnoreCase(userName, Status.ACTIVE.toString());
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + "user details---->" + user);
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while getting user by username : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : "
					+ Arrays.toString(e.getStackTrace()));
			return new Response<User>(500, false, HttpStatus.INTERNAL_SERVER_ERROR,
					"Error while getting user by username").setResponse(null);
		}

		if (user == null) {
			return new Response<User>(404, false, HttpStatus.NOT_FOUND, "No user found with User name : " + userName)
					.setResponse(null);
		}

		return new Response<User>(200, true, HttpStatus.OK, "User retrieved successfully").setResponse(user);

	}
}
