package com.widget.CustomWidgetReport.entity;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

@Data

@Table(name="SUPPORT_TYPES")
public class SupportType {
	

	@Column(name="PRIORITY", nullable=true)
	private String priority;
	
	@Column(name="SUPPORT_TYPE", nullable=true)
	private String supportType;

	
}
