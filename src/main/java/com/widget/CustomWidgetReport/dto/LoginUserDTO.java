package com.widget.CustomWidgetReport.dto;



import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.widget.CustomWidgetReport.entity.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUserDTO extends Response {

	private static final long serialVersionUID = 1L;

	private Integer userId;

	@Size(min = 3, max = 60)
	private String username;

	private String password;

	private String displayName;

	private String type = "Bearer";

	private String accessToken;

	private Collection<? extends GrantedAuthority> authorities;
	// private List<LocationDTO> locations;

	private Role role;

	private String firstName;

	private String lastName;

	private String email;

	private String securityQuestion;

	private String answer;

	private String status;

	private List<String> features;

	public LoginUserDTO() {
	}

	public LoginUserDTO(Integer userId, String username, Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.authorities = authorities;
	}

}