package com.amazon.documentdb.service;

import com.amazon.documentdb.model.Message;

public interface MessageValidatorService {
	
	boolean isValid(Message message);

}
