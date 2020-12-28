package com.amazonaws.lambda.rds.model;

import java.util.Date;

import org.bson.Document;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;

public class Message {

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

	public Document toDocument() {
		BasicDBObject document = new BasicDBObject();
		document.put("messageId", messageId);
		document.put("payload", payload);
		document.put("createdBy", createdBy);
		document.put("createdOn", createdOn.toString());
		return new Document(document);
	}
	
	public JsonObject toJson() {
		final JsonObject messageJson = new JsonObject();
		messageJson.addProperty("messageId", messageId);
		messageJson.addProperty("payload", payload);
		messageJson.addProperty("createdBy", createdBy);
		return messageJson;
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
