package com.amazon.documentdb.service;

import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;

public interface MessageLoggerService {
	
	MessageLog save(Message message);

}
