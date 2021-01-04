package com.lambda.mongodb.mongodbpersister.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambda.mongodb.mongodbpersister.dao.MessageRepository;
import com.lambda.mongodb.mongodbpersister.exception.MessageNotFoundException;
import com.lambda.mongodb.mongodbpersister.model.Message;

@Service
public class MessageServiceImpl implements MessageService {

	private MessageRepository messageRepository;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public List<Message> save(List<Message> messages) {
		return this.messageRepository.saveAll(messages);
	}

	@Override
	public Message getById(Long messageid) {
		return this.messageRepository.findById(messageid)
				.orElseThrow(() -> new MessageNotFoundException("Message not found."));
	}

}
