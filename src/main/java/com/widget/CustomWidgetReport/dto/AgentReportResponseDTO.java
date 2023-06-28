package com.widget.CustomWidgetReport.dto;

import lombok.Data;

@Data
public class AgentReportResponseDTO {	
	private String name;
	private String contact;
	private String type;
	private String offered;
	private String answered;
	private String abandoned;
	private String PerAnswered;
}
