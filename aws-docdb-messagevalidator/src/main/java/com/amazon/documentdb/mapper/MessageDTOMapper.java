package com.amazon.documentdb.mapper;

import org.springframework.stereotype.Component;

import com.amazon.documentdb.model.Message;
import com.persistent.common.domain.MessageDto;

@Component
public class MessageDTOMapper implements DTOEntityMapper<Message, MessageDto> {

	@Override
	public MessageDto toDto(Message message) {
		return null;
	}

	@Override
	public Message toEntity(MessageDto messageDto) {
		return new Message(messageDto.getMessageId(), 
				messageDto.getPayload(), messageDto.getCreatedOn(), messageDto.getCreatedBy());
	}

}
