package com.lambda.mongodb.mongodbpersister.model;

public class WebResponse {
	
	private String responseMessage;

	public WebResponse(String errorMessage) {
		this.responseMessage = errorMessage;
	}

	public String getErrorMessage() {
		return responseMessage;
	}

}
