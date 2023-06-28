package com.widget.CustomWidgetReport.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.widget.CustomWidgetReport.dto.LoginUserDTO;
import com.widget.CustomWidgetReport.dto.ResponseDto;
import com.widget.CustomWidgetReport.dto.UserDto;
import com.widget.CustomWidgetReport.entity.Feature;
import com.widget.CustomWidgetReport.entity.User;
import com.widget.CustomWidgetReport.repository.UserRepository;
import com.widget.CustomWidgetReport.util.Aes;
import com.widget.CustomWidgetReport.util.CustomWidgetLog;
import com.widget.CustomWidgetReport.util.EntityConstants.Status;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class LoginController {
	
	private static final Logger log = LogManager.getLogger("LoginController"); 
	@Autowired
	private EhCacheBasedUserCache userCache;

	@Autowired
	UserRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Aes aes;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDto loginRequest) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		log.info(CustomWidgetLog.getCurrentClassAndMethodName() + " ---> Begin Login service ");
		User userDtls = modelMapper.map(loginRequest, User.class);
		userDtls = usersRepository.findByUserNameIgnoreCaseAndStatusIgnoreCase(loginRequest.getUserName(),
				Status.ACTIVE.toString());
		log.info(CustomWidgetLog.getCurrentClassAndMethodName() + userDtls);
		if (userDtls != null) {

			String dbPassword = aes.decrypt(userDtls.getPassword());
			if (loginRequest.getPassword().equals(dbPassword)) {

				LoginUserDTO userDTO = modelMapper.map(userDtls, LoginUserDTO.class);

				Set<Feature> featureObjs = userDtls.getRole().getFeatures();
				List<String> features = featureObjs.stream().map(f -> f.getFeatureName()).collect(Collectors.toList());
				userDTO.setFeatures(features);
				
				log.info(CustomWidgetLog.getCurrentClassAndMethodName() + " ---> End Login service ");
				userDTO.setStatusCode(HttpStatus.OK);
				return ResponseEntity.ok(userDTO);
			} else {

				return new ResponseEntity<>(new ResponseDto("Please enter vaild credentials!", HttpStatus.BAD_REQUEST),
						HttpStatus.OK);
			}
		} else {
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + "---> End Login service");
			return new ResponseEntity<>(new ResponseDto("Please enter vaild credentials!", HttpStatus.BAD_REQUEST),
					HttpStatus.OK);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@Valid @RequestBody LoginUserDTO userDTO) {
		log.info(CustomWidgetLog.getCurrentClassAndMethodName() + "---> Begin logout service");

		userCache.removeUserFromCache(userDTO.getUsername());
		log.info(CustomWidgetLog.getCurrentClassAndMethodName() + "--->End logout service");
		return new ResponseEntity<>(new ResponseDto("User Logout successfully!", HttpStatus.OK), HttpStatus.OK);
	}

}
