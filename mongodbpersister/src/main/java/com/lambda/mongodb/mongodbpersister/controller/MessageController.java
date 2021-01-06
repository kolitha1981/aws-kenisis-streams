package com.lambda.mongodb.mongodbpersister.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lambda.mongodb.mongodbpersister.model.ErrorMessage;
import com.lambda.mongodb.mongodbpersister.model.Message;
import com.lambda.mongodb.mongodbpersister.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> save(@RequestBody List<Message> messages) {
		try {
			return new ResponseEntity<>(this.messageService.save(messages), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/messages/{messageId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> getById(@PathVariable("messageId") Long messageId) {
		try {
			return new ResponseEntity<>(this.messageService.getById(messageId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
