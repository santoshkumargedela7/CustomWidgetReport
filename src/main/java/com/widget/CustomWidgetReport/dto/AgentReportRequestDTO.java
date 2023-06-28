package com.widget.CustomWidgetReport.dto;

import com.sun.istack.NotNull;

import lombok.Data;
@Data
public class AgentReportRequestDTO {
	@NotNull
	private String startDate;
	
	@NotNull
	private String endDate;
	
	@NotNull
	private String dateRange;
	
	private String type;
}
