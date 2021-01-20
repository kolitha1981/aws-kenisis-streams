package com.lambda.mongodb.mongodbpersister.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lambda.mongodb.mongodbpersister.dao.MessageRepository;
import com.lambda.mongodb.mongodbpersister.exception.MessageNotFoundException;
import com.lambda.mongodb.mongodbpersister.exception.MessageProcessingFailedException;
import com.lambda.mongodb.mongodbpersister.model.Message;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
	@HystrixCommand(fallbackMethod = "geEmptySavedList")
	public List<Message> save(List<Message> messages) {
		final List<Message> savedListOfMessages = this.messageRepository.saveAll(messages);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<List<Message>> messagerequestBody = new HttpEntity<>(savedListOfMessages,
				httpHeaders);
		final String logEndPointURL = "http://" + logServiceName + "/messages/batch";
		try {
			final ResponseEntity responseEntity = this.restTemplate.postForEntity(logEndPointURL, messagerequestBody,
					ResponseEntity.class);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				return messages;
			} else {
				throw new MessageProcessingFailedException("Remote http request failed.");
			}
		} catch (Exception e) {
			throw new MessageProcessingFailedException(e.getMessage(), e);
		}

	}
	
	private List<Message> geEmptySavedList(){
		return Collections.emptyList();
	}

	@Override
	public Message getById(Long messageid) {
		return this.messageRepository.findById(messageid)
				.orElseThrow(() -> new MessageNotFoundException("Message not found."));
	}

}
