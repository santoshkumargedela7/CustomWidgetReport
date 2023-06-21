package com.widget.CustomWidgetReport.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity 
@Table(name="features_tbl")
public class Feature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id" , nullable=false)
	private Integer id;
	
	@Column(name="featureName" , nullable=false)
	private String featureName;
    
	@Column(name="status" , nullable=false)
	private String status;
	
	@Column(name="createdOn" , nullable=true)
	private String createdOn;
	
	@Column(name="updatedOn" , nullable=true)
	private String updatedOn;
	
}
