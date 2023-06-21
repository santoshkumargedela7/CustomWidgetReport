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
@Table(name="SUPPORT_TYPES_TBL")
public class SupportType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="PRIORITY", nullable=true)
	private String priority;
	
	@Column(name="SUPPORT_TYPE", nullable=true)
	private String supportType;

	
}
