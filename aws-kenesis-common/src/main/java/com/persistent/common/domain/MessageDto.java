package com.persistent.common.domain;

import java.util.Date;

public class MessageDto {

	private Long messageId;
	private String payload;
	private Date createdOn;
	private String createdBy;

	public MessageDto(Long messageId, String payload, Date createdOn, String createdBy) {
		this.messageId = messageId;
		this.payload = payload;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}

	public Long getMessageId() {
		return messageId;
	}

	public String getPayload() {
		return payload;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public MessageLogDto getLog() {
		final MessageLogDto messageLog = new MessageLogDto();
		messageLog.setMessageCreatedBy(getCreatedBy());
		messageLog.setMessageCreatedOn(getCreatedOn());
		messageLog.setMessagePayLoad(getPayload());
		return messageLog;

	}

	@Override
	public int hashCode() {
		return 31 + ((messageId == null) ? 0 : messageId.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MessageDto other = (MessageDto) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		return true;
	}

}
