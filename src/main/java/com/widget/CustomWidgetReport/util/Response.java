package com.widget.CustomWidgetReport.util;

import org.springframework.http.HttpStatus;

public class Response<T> {
	
	private Integer statusCode;
	private Boolean isSuccess;
	private String message;
	private HttpStatus status;

	public T response;
	
	public Response(Integer statusCode, Boolean isSuccess, HttpStatus status, String message) {
		this.statusCode = statusCode;
		this.isSuccess = isSuccess;
		this.status = status;
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}
	
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public T getResponse() {
		return response;
	}

	public Response<T> setResponse(T response) {
		this.response = response;
		return this;
	}


}
