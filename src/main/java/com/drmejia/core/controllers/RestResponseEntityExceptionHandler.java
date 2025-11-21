package com.drmejia.core.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.drmejia.core.exceptions.BadRequestException;



@ControllerAdvice
public class RestResponseEntityExceptionHandler 
extends ResponseEntityExceptionHandler{
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequets(
    RuntimeException ex, WebRequest request){
            
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("massage", ex.getMessage());
        body.put("Error", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}

    


