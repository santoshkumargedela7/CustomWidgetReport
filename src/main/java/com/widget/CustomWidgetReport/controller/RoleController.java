package com.widget.CustomWidgetReport.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.widget.CustomWidgetReport.dto.RoleDto;
import com.widget.CustomWidgetReport.entity.Feature;
import com.widget.CustomWidgetReport.entity.Role;
import com.widget.CustomWidgetReport.entity.SupportType;
import com.widget.CustomWidgetReport.service.FeatureService;
import com.widget.CustomWidgetReport.service.RoleService;
import com.widget.CustomWidgetReport.util.CustomWidgetLog;
import com.widget.CustomWidgetReport.util.EntityConstants.Status;
import com.widget.CustomWidgetReport.util.Response;

import lombok.extern.log4j.Log4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
@Log4j
public class RoleController {
	@Autowired 
	private Environment env;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private FeatureService featureService;
	
	@PostMapping("/save")
	public Response<Role> createrole(@RequestBody RoleDto roleDto) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 

		Set<Feature> features = new HashSet<>();
		if (roleService.existsByRoleNameIgnoreCase(roleDto.getRoleName())) {
			return new Response<Role>(400, false, HttpStatus.BAD_REQUEST, "RoleName already In Use").setResponse(null);
		}
		String[] featureIds = roleDto.getFeatureIds().split(",");
		for(String i: featureIds) {
			features.add(featureService.findById(Integer.parseInt(i)));
		}
		
		Role role = new Role();
		try {
		role.setRoleName(roleDto.getRoleName());
		role.setStatus(roleDto.getStatus());
		role.setFeatures(features);
		role.setCreatedBy(roleDto.getCreatedBy());
		role.setCreatedOn(LocalDateTime.now().format(format));
		role.setSupportType(roleDto.getSupportType());
				
		role = roleService.save(role);
		log.info(CustomWidgetLog.getCurrentClassAndMethodName() + role);
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() +"Error while saving role : " + e);
			log.error("stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<Role>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating role").setResponse(null);
		}
		return new Response<Role>(200, true, HttpStatus.OK, "Role has been created").setResponse(role);
	}
	
	@GetMapping("/list")
	public Response<List<Role>> getAllRoles() {
		List<Role> roles = new ArrayList<>();
		try {
			roles = roleService.findAll();
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while listing role : " + e);
			log.error("stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<List<Role>>(500, false,HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting roles").setResponse(null);
		}
		return new Response<List<Role>>(200, true, HttpStatus.OK, roles.size() +" role has been retrived").setResponse(roles);
	}
	
	@GetMapping("/listByStatus")
	public Response<List<Role>> getRoles() {
		List<Role> roles = new ArrayList<>();
		try {
			roles = roleService.getRolesByStatus(Status.ACTIVE.toString());
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + roles);
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while listing role by status : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<List<Role>>(500, false,HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting roles").setResponse(null);
		}
		return new Response<List<Role>>(200, true, HttpStatus.OK, roles.size() +" role has been retrived").setResponse(roles);
	}
	
	
	@DeleteMapping("/delete/{roleId}")
	public Response<String> deleteUserById(@PathVariable Integer roleId) {
		try {
			
			Role role = null;
			try {
			role = roleService.findById(roleId);
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + role);
			} catch (Exception e) {
				return new Response<String>(500, false,HttpStatus.NOT_FOUND, "No role found with the roleId : " + roleId).setResponse(null);
			}
			roleService.deleteById(roleId);
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while delete by role id : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<String>(500, false,HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting role").setResponse(null);
		}
		return new Response<String>(200, true, HttpStatus.OK, "Role has been deleted").setResponse(null);
	}
	
	
	
	@PutMapping("/update/{roleId}")
	public Response<Role> updaterole(@PathVariable Integer roleId, @RequestBody RoleDto roleDto) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		Role role = null;
		
		Set<Feature> features = new HashSet<>();
		try {
		String[] featureIds = roleDto.getFeatureIds().split(",");
		for(String i: featureIds) {
			features.add(featureService.findById(Integer.parseInt(i)));
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + features);
		}

			if (roleDto != null) {
				role= roleService.findById(roleId);
				role.setRoleName(roleDto.getRoleName());
				role.setStatus(roleDto.getStatus());
				role.setFeatures(features);
				role.setUpdatedBy(roleDto.getUpdatedBy());
				role.setUpdatedOn(LocalDateTime.now().format(format));
				role.setSupportType(roleDto.getSupportType());
				//role.setCampaignDesc(roleDto.getCampaignDesc());
				role = roleService.save(role);
				log.info(CustomWidgetLog.getCurrentClassAndMethodName() + role);
			}
		}catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while updating role : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : " + Arrays.toString(e.getStackTrace()));
				return new Response<Role>(500, false, HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating role").setResponse(null);
			}
			return new Response<Role>(200, true, HttpStatus.OK, "Role has been updated").setResponse(role);
		
	}
	
	
	@GetMapping("/typelist")
	public Response<List<SupportType>> getTypes() {
		List<SupportType> supportTypes = new ArrayList<>();
		SupportType supportType = null;
		String pom_driver = env.getProperty("spring.datasource.driver-class-name");
		String pom_url = env.getProperty("spring.datasource.url");
		String pom_user = env.getProperty("spring.datasource.username");
		String pom_password = env.getProperty("spring.datasource.password");
		String supportQuery = env.getProperty("getTypes");
		Connection pom_con = null;
		try {
			Class.forName(pom_driver);
			pom_con = DriverManager.getConnection(pom_url, pom_user, pom_password);
			Statement stmt = pom_con.createStatement();
			ResultSet rs = stmt.executeQuery(supportQuery);
			while(rs.next()) {
				supportType = new SupportType();
				supportType.setPriority(rs.getString("priority"));
				supportType.setSupportType(rs.getString("support_type"));
				supportTypes.add(supportType);
			}
			
			log.info(CustomWidgetLog.getCurrentClassAndMethodName() + supportTypes);
			
		} catch (Exception e) {
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "Error while getting supportType : " + e);
			log.error(CustomWidgetLog.getCurrentClassAndMethodName() + "stack trace : " + Arrays.toString(e.getStackTrace()));
			return new Response<List<SupportType>>(500, false,HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting supportType").setResponse(null);
		}
		return new Response<List<SupportType>>(200, true, HttpStatus.OK, supportTypes.size() +" supportType has been retrieved").setResponse(supportTypes);
	}
	
	
	
	

}
