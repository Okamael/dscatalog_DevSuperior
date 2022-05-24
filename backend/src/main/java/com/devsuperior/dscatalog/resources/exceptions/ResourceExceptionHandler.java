package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = StandardError.builder()
				.timestamp(Instant.now())
				.status(status.value())
				.error("Rersource not found  ")
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(status).body(err);
		
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBase(DataBaseException e, HttpServletRequest request){
		HttpStatus status =HttpStatus.BAD_REQUEST;
		StandardError err = StandardError.builder()
				.timestamp(Instant.now())
				.status(status.value())
				.error("Database Exception ")
				.message(e.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(status).body(err);
		
	}
}

