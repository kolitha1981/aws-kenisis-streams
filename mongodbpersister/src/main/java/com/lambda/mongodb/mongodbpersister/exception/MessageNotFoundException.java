package com.lambda.mongodb.mongodbpersister.exception;

public class MessageNotFoundException  extends RuntimeException{

	public MessageNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessageNotFoundException(String message) {
		super(message);
	}
	
}
