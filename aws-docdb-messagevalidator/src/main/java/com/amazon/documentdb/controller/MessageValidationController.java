package com.amazon.documentdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;
import com.amazon.documentdb.service.MessageLoggerService;

@RestController
public class MessageValidationController {

	@Autowired
	private MessageLoggerService messageValidatorService;

	@GetMapping(path = "verification/{messageId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> checkValidity(@RequestBody Message message) {
		final MessageLog messageLog = messageValidatorService.save(message);
		return new ResponseEntity<>(messageLog, HttpStatus.OK);
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> health() {
		return new ResponseEntity<>(Boolean.valueOf(true), HttpStatus.OK);
	}

}
