package com.widget.CustomWidgetReport.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class Response implements Serializable {
 	
	private static final long serialVersionUID = 8752809625219594448L;
	
	private HttpStatus statusCode;
	@JsonInclude(Include.NON_NULL)
	private String message;
	
	public Response() {}
	
	public Response(HttpStatus statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
}