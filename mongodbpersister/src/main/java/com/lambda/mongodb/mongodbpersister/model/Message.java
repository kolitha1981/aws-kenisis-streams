package com.lambda.mongodb.mongodbpersister.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class Message {
	
	@Id
	private Long messageId;
	private String payload;
	private Date createdOn;
	private String createdBy;

	public Message(Long messageId, String payload, Date createdOn, String createdBy) {
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
		Message other = (Message) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		return true;
	}
	

}
