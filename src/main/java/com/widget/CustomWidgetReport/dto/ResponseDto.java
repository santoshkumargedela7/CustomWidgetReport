package com.widget.CustomWidgetReport.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseDto {
	private HttpStatus statusCode;
	private String message;
	public ResponseDto(String message,HttpStatus statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}
	

}
