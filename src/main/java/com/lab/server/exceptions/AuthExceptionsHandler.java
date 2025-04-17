package com.lab.server.exceptions;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lab.lib.api.ApiResponse;
import com.lab.server.configs.language.MessageSourceHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthExceptionsHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	@Autowired
	private MessageSourceHelper messageHelper;
	
	private String getMessage(String key) {
		return messageHelper.getMessage(key);
	}

    /* 401 unauthorized */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
            
            String message = getMessage("warning.unauthorized");
            String detailMessage = exception.getLocalizedMessage();
     
            String moreInformation = "http://localhost:8080/api/v1/exception/401";
            
            ApiResponse<String> errorResponse = new ApiResponse<String>(false, message, detailMessage, HttpStatus.UNAUTHORIZED, moreInformation);
            log.error(detailMessage, exception);
            
            // convert object to json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(errorResponse);

            // return json
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
    }
    
    /* 403 Forbidden */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException exception) throws IOException, ServletException {
            
            String message = getMessage("warning.unauthorized");
            String detailMessage = exception.getLocalizedMessage();
            String moreInformation = "http://localhost:8080/api/v1/exception/403";
            
            ApiResponse<String> errorResponse = new ApiResponse<String>(false, message, detailMessage, HttpStatus.FORBIDDEN, moreInformation);
            log.error(detailMessage, exception);
            
            // convert object to json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(errorResponse);

            // return json
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
    } 

}

