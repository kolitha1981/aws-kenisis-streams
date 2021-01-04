package com.lambda.mongodb.mongodbpersister.service;

import java.util.List;

import com.lambda.mongodb.mongodbpersister.model.Message;

public interface MessageService {

	List<Message> save(List<Message> messages);
	
	Message getById(Long messageid);

}
