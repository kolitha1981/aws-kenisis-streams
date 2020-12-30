package com.amazonaws.lambda.kenesis.model;

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

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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


}
