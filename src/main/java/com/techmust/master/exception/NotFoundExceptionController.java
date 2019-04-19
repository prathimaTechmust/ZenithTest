package com.techmust.master.exception;

public class NotFoundExceptionController extends RuntimeException
{

	public NotFoundExceptionController(String message, Throwable cause)
	{
		super(message, cause);		
	}

	public NotFoundExceptionController(String message)
	{
		super(message);		
	}

	public NotFoundExceptionController(Throwable cause)
	{
		super(cause);		
	}	
}
