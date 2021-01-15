package com.amazon.documentdb.service;

import java.util.List;

import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;

public interface MessageLoggerService {
	
	MessageLog save(Message message);
	
	List<MessageLog> save(List<Message> message);

}
