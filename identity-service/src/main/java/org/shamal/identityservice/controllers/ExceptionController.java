package org.shamal.identityservice.controllers;

import org.shamal.identityservice.exceptions.InvalidInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> invalidInputExceptionHandler(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
