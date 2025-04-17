package com.lab.lib.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizationException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;

	public UnAuthorizationException(String message) {
		super(message);
	}
}
