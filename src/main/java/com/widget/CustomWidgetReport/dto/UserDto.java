package com.widget.CustomWidgetReport.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends Response implements Serializable {

	private static final long serialVersionUID = 3747230938101136531L;

	private Integer id;
	private String userName;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private Integer roleId;
	private String securityQuestion;
	private String answer;

	
}
