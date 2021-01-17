package com.lambda.mongodb.mongodbpersister.exception;

public class MessageProcessingFailedException extends RuntimeException {

	public MessageProcessingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageProcessingFailedException(String message) {
		super(message);
	}

}
