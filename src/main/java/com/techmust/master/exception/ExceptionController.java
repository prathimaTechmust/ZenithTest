package com.techmust.master.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController
{
	@ExceptionHandler
	public ResponseEntity<ExceptionResponse> handleAllException(Exception eException)
	{
		ExceptionResponse oExceptionResponse = new ExceptionResponse();
		oExceptionResponse.setStatus(HttpStatus.NOT_FOUND.value());
		oExceptionResponse.setMessage(eException.getMessage());
		oExceptionResponse.setTimeStamp(System.currentTimeMillis());
		return new ResponseEntity<>(oExceptionResponse,HttpStatus.NOT_FOUND);	
	}
}
