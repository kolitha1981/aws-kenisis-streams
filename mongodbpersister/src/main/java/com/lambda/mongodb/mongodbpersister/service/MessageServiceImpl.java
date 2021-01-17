package com.lambda.mongodb.mongodbpersister.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lambda.mongodb.mongodbpersister.dao.MessageRepository;
import com.lambda.mongodb.mongodbpersister.exception.MessageNotFoundException;
import com.lambda.mongodb.mongodbpersister.model.Message;

@Service
public class MessageServiceImpl implements MessageService {

	private MessageRepository messageRepository;	
	private RestTemplate restTemplate;
	@Value("${org.persistent.mongodb.logservice.name}")
	private String logServiceName;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository, RestTemplate restTemplate) {
		this.messageRepository = messageRepository;
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Message> save(List<Message> messages) {
		final List<Message> savedListOfMessages = this.messageRepository.saveAll(messages);
		final HttpHeaders httpHeaders =  new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<List<Message>> messagerequestBody =  
				new HttpEntity<List<Message>>(savedListOfMessages, httpHeaders);
		final String logEndPointURL = "http://"+logServiceName+"//messages/batch";
		this.restTemplate.postForEntity(logEndPointURL, messagerequestBody, ResponseEntity<List<Mes>>)
		
	}

	@Override
	public Message getById(Long messageid) {
		return this.messageRepository.findById(messageid)
				.orElseThrow(() -> new MessageNotFoundException("Message not found."));
	}

}
