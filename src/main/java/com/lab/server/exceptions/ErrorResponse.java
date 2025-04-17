package com.lab.server.exceptions;

import java.util.List;
import java.util.Map;

public class ErrorResponse {
    private String message;
    private String detailMessage;
    private Map<String,String> errors;
    private int code;
    private String moreInformation;
    
	public ErrorResponse() {
	}

	public ErrorResponse(String message, String detailMessage, Map<String, String> errors, int code,
			String moreInformation) {
		this.message = message;
		this.detailMessage = detailMessage;
		this.errors = errors;
		this.code = code;
		this.moreInformation = moreInformation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMoreInformation() {
		return moreInformation;
	}

	public void setMoreInformation(String moreInformation) {
		this.moreInformation = moreInformation;
	}
	
    
    }
