package com.amazon.documentdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;
import com.amazon.documentdb.service.MessageLoggerService;

@RestController
public class MessageLogController {

	@Autowired
	private MessageLoggerService messageLogService;

	@PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> save(@RequestBody Message message) {
		final MessageLog messageLog = messageLogService.save(message);
		return new ResponseEntity<>(messageLog, HttpStatus.OK);
	}
	
	@PostMapping(path = "/messages/batch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> saveAll(@RequestBody List<Message> messages) {
		final List<MessageLog> messageLogs = messageLogService.save(messages);
		return new ResponseEntity<>(messageLogs, HttpStatus.OK);
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> health() {
		return new ResponseEntity<>(Boolean.valueOf(true), HttpStatus.OK);
	}

}
