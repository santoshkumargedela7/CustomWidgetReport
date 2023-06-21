package com.widget.CustomWidgetReport.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="users_tbl")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id" , nullable=false)
	private Integer id;
	
	@Column(name="user_name" , nullable=false)
	private String userName;
	
	@Column(name="status" , nullable=false)
	private String status;
	
	@Column(name="created_on" , nullable=true)
	private String createdOn;
	
	@Column(name="updatedOn" , nullable=true)
	private String updatedOn;
	
	@Column(name="createdBy" , nullable=true)
	private String createdBy;
	
	@Column(name="updatedBy" , nullable=true)
	private String updatedBy;
	
	@Column(name="password" , nullable=false)
	private String password;
	
	@Column(name="firstName" , nullable=true)
	private String firstName;
	
	@Column(name="lastName" , nullable=true)
	private String lastName;
	
	@Column(name="email" , nullable=false)
	private String email;
	
	@Column(name="gender" , nullable=true)
	private String gender;
	
	@Column(name="securityQuestion", nullable=true)
	private String securityQuestion;
	
	@Column(name="answer", nullable=true)
	private String answer;
	
	@OneToOne(targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
	private Role role;
	

}
