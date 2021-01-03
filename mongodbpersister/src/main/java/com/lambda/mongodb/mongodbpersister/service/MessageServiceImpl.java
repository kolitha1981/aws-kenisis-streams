package com.lambda.mongodb.mongodbpersister.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambda.mongodb.mongodbpersister.dao.MessageRepository;
import com.lambda.mongodb.mongodbpersister.model.Message;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public List<Message> save(List<Message> messages) {
		return this.messageRepository.saveAll(messages);
	}

}
