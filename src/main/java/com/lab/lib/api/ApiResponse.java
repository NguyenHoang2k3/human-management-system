package com.lab.lib.api;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean status;
    private String message;
    
    @JsonIgnoreProperties
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonIgnoreProperties
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpStatus httpCode;
    @JsonIgnoreProperties
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String moreInformation;

    public ApiResponse(boolean status) {
        this.status = status;
        if(status){
            this.message = "Success";
        } else {
            this.message = "FAILED!";
        }
    }

    public ApiResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(boolean status, T data) {
        this.status = status;
        if(status){
            this.message = "Success";
        } else {
            this.message = "FAILED!";
        }
        this.data = data;
    }
    
    public ApiResponse(boolean status, String message, T data, HttpStatus httpCode, String moreInformation) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.httpCode = httpCode;
        this.moreInformation = moreInformation;
    }
}