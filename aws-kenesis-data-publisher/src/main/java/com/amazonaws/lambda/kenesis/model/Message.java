package com.amazonaws.lambda.kenesis.model;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Message {

	private Long messageId;
	private String payload;
	private Date createdOn;
	private String createdBy;

	private Message(Long messageId, String payload, Date createdOn, String createdBy) {
		this.messageId = messageId;
		this.payload = payload;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}

	public String toJson() {
		final JsonObject messageJson = new JsonObject();
		messageJson.addProperty("messageId", messageId);
		messageJson.addProperty("payload", payload);
		messageJson.addProperty("createdBy", createdBy);
		return new Gson().toJson(messageJson);
	}

	public static class MessageBuilder {

		private Long messageId;
		private String payload;
		private Date createdOn;
		private String createdBy;

		public MessageBuilder(Long messageId, String payload) {
			super();
			this.messageId = messageId;
			this.payload = payload;
		}

		public MessageBuilder addCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
			return this;
		}

		public MessageBuilder addCreatedBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public Message build() {
			return new Message(this.messageId, this.payload, this.createdOn, this.createdBy);
		}

	}

}
