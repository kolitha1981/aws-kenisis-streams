package com.amazon.documentdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.documentdb.exception.MessageValidatorNotFoundException;
import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageValidator;
import com.amazon.documentdb.repository.AuroraRepository;

@Service
public class MessageValidatorServiceImpl implements MessageValidatorService {

	@Autowired
	private AuroraRepository auroraRepository;

	@Override
	public boolean isValid(Message message) {
		final MessageValidator messageValidator = this.auroraRepository.findById(1L)
				.orElseThrow(() -> new MessageValidatorNotFoundException("Message validator not found."));
		return messageValidator.getPayLoadMaxCharLimit() <= message.getPayload().length();
	}

}
