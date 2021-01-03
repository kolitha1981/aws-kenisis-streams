package com.lambda.mongodb.mongodbpersister.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lambda.mongodb.mongodbpersister.model.Message;
import com.lambda.mongodb.mongodbpersister.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> save(@RequestBody List<Message> messages) {
		try {
			final List<Message> savedMessages = this.messageService.save(messages);
			return new ResponseEntity<>(savedMessages, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
