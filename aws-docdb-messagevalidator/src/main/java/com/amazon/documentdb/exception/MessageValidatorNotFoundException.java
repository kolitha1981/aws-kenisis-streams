package com.amazon.documentdb.exception;

public class MessageValidatorNotFoundException extends RuntimeException {

	public MessageValidatorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageValidatorNotFoundException(String message) {
		super(message);
	}
	
}
