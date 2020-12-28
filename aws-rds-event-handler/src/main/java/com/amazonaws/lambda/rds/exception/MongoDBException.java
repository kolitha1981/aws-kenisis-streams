package com.amazonaws.lambda.rds.exception;

public class MongoDBException extends RuntimeException {

	public MongoDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public MongoDBException(String message) {
		super(message);
	}

}
