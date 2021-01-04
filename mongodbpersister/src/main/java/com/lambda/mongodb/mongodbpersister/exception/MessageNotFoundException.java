package com.lambda.mongodb.mongodbpersister.exception;

public class MessageNotFoundException  extends RuntimeException{

	public MessageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageNotFoundException(String message) {
		super(message);
	}
	
}
