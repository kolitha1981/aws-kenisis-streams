package com.amazon.documentdb.service;

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

}
