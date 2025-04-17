package com.lab.server.exceptions;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
//import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class RestExceptionsHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	private String getMessage(String key) {
		return messageSource.getMessage(key, null, "Default message", LocaleContextHolder.getLocale());
	}

	public RestExceptionsHandler() {
		super(); // Gọi constructor cha
		log.info("🚀 RestExceptionHandler is loaded!");
	}

	// 1. Default Exception

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception exception) {
		String message = getMessage("Exception.message");
		String detailMessage = exception.getLocalizedMessage();
		int code = 500;
		String moreInformation = "http://localhost:8080/api/v1/exception/500";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 2. Not found URL handler
	@Override	
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception,
			org.springframework.http.HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String message = getMessage("NoHandlerFoundException.message") + exception.getHttpMethod() + " "
				+ exception.getRequestURL();
		String detailMessage = exception.getLocalizedMessage();
		int code = 404;
		String moreInformation = "http://localhost:8080/api/v1/exception/404";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, status);
	}

	// 3. Not support HTTP Method  

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception, org.springframework.http.HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		log.error("🚨 Exception caught: ", exception);
		String message = exception.getMethod() + " " + getMessage("HttpRequestMethodNotSupportedException.message");
		String detailMessage = exception.getLocalizedMessage();
		int code = 405;
		String moreInformation = "http://localhost:8080/api/v1/exception/405";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, status);

	}

	// 4. Not support media type

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
			org.springframework.http.HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String message = exception.getContentType() + " " + getMessage("HttpMediaTypeNotSupportedException.message");
		String detailMessage = exception.getLocalizedMessage();
		int code = 415;
		String moreInformation = "http://localhost:8080/api/v1/exception/415";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, status);
	}

	// 5. Validation error via annotation (@NotNull, @Size, ...)

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			org.springframework.http.HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String message = getMessage("MethodArgumentNotValidException.message");
		String detailMessage = exception.getLocalizedMessage();
		Map<String, String> errors = new HashMap<>();

		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		}
		int code = 400;
		String moreInformation = "http://localhost:8080/api/v1/exception/400";

		ErrorResponse response = new ErrorResponse(message, detailMessage, errors, code, moreInformation);
		log.error(detailMessage + "\n" + errors, exception);
		return new ResponseEntity<>(response, status);
	}

	// 6. Validation error via custom annotation

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {

		String message = getMessage("MethodArgumentNotValidException.message");
		String detailMessage = exception.getLocalizedMessage();
		Map<String, String> errors = new HashMap<>();

		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			String fieldName = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(fieldName, errorMessage);
		}
		int code = 400;
		String moreInformation = "http://localhost:8080/api/v1/exception/400";

		ErrorResponse response = new ErrorResponse(message, detailMessage, errors, code, moreInformation);
		log.error(detailMessage + "\n" + errors, exception);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// 7. Missing request parameter

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException exception, org.springframework.http.HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		String message = exception.getParameterName() + " "
				+ getMessage("MissingServletRequestParameterException.message");
		String detailMessage = exception.getLocalizedMessage();
		int code = 400;
		String moreInformation = "http://localhost:8080/api/v1/exception/400";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, status);
	}

	// 8. Incorrect path variable type

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {

		String message = exception.getName() + " " + getMessage("MethodArgumentTypeMismatchException.message")
				+ exception.getRequiredType().getName();
		String detailMessage = exception.getLocalizedMessage();
		int code = 400;
		String moreInformation = "http://localhost:8080/api/v1/exception/400";

		ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
		log.error(detailMessage, exception);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
