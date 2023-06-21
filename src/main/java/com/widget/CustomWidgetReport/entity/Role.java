package com.widget.CustomWidgetReport.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="roles_tbl")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id" , nullable=false)
	private Integer id;
	
	@Column(name="roleName" , nullable=false)
	private String roleName;
	
	@Column(name="status" , nullable=false)
	private String status;

	
	@Column(name="createdOn" , nullable=true)
	private String createdOn;
	
	@Column(name="updatedOn" , nullable=true)
	private String updatedOn;
	
	@Column(name="createdBy" , nullable=true)
	private String createdBy;
	
	@Column(name="updatedBy" , nullable=true)
	private String updatedBy;
	
	@Column(name="supportTypeId" , nullable=true)
	private String supportType;
	
	@ManyToMany(targetEntity = Feature.class)
	@JoinTable(name="role_features_tbl",
		joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id"))
	private Set<Feature> features = new HashSet<>();
	

 
}
