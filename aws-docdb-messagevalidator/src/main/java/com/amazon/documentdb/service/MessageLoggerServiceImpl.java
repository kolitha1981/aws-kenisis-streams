package com.amazon.documentdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;
import com.amazon.documentdb.repository.AuroraRepository;

@Service
public class MessageLoggerServiceImpl implements MessageLoggerService {

	@Autowired
	private AuroraRepository auroraRepository;

	@Override
	public MessageLog save(Message message) {
		return this.auroraRepository.save(message.getLog());
	}

	@Override
	public List<MessageLog> save(List<Message> messages) {
		final List<MessageLog> savedMessageLogs = new ArrayList<>();
		List<MessageLog> messageLogs = messages.stream().map((message) -> {
			return message.getLog();
		}).collect(Collectors.toList());
		this.auroraRepository.saveAll(messageLogs).forEach((messageLog) -> {
			savedMessageLogs.add(messageLog);
		});
		return savedMessageLogs;
	}

}
