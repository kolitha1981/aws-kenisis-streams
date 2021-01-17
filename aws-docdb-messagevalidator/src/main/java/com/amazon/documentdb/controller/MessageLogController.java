package com.amazon.documentdb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.documentdb.mapper.MessageDTOMapper;
import com.amazon.documentdb.mapper.MessageLogDTOMapper;
import com.amazon.documentdb.model.MessageLog;
import com.amazon.documentdb.service.MessageLoggerService;
import com.persistent.common.domain.MessageDto;

@RestController
public class MessageLogController {

	@Autowired
	private MessageLoggerService messageLogService;
	@Autowired
	private MessageLogDTOMapper messageLogDTOMapper;
	@Autowired
	private MessageDTOMapper messageDtoMapper;

	@PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> save(@RequestBody MessageDto messageDto) {
		final MessageLog messageLog = messageLogService.save(messageDtoMapper.toEntity(messageDto));
		return new ResponseEntity<>(messageLogDTOMapper.toDto(messageLog), HttpStatus.OK);
	}
	
	@PostMapping(path = "/messages/batch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> saveAll(@RequestBody List<MessageDto> messageDtos) {
		final List<MessageLog> messageLogs = messageLogService.save(messageDtos.stream()
				.map((messageDto)-> {return messageDtoMapper.toEntity(messageDto);}).collect(Collectors.toList()));
		return new ResponseEntity<>(messageLogs, HttpStatus.OK);
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> health() {
		return new ResponseEntity<>(Boolean.valueOf(true), HttpStatus.OK);
	}

}
