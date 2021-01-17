package com.amazon.documentdb.mapper;

import org.springframework.stereotype.Component;

import com.amazon.documentdb.model.MessageLog;
import com.persistent.common.domain.MessageLogDto;

@Component
public class MessageLogDTOMapper implements DTOEntityMapper<MessageLog, MessageLogDto> {

	@Override
	public MessageLogDto toDto(MessageLog messageLog) {
		final MessageLogDto messageLogDto = new MessageLogDto();
		messageLogDto.setMessageLogId(messageLog.getMessageLogId());
		messageLogDto.setMessageCreatedOn(messageLog.getMessageCreatedOn());
		messageLogDto.setMessageCreatedBy(messageLog.getMessageCreatedBy());
		messageLogDto.setMessagePayLoad(messageLog.getMessagePayLoad());
		return messageLogDto;
	}

	@Override
	public MessageLog toEntity(MessageLogDto messageLogDto) {
		return null;
	}

}
