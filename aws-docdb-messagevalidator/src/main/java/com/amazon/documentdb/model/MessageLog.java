package com.amazon.documentdb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_LOG")
public class MessageLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MESSAGE_LOG_ID")
	private Long messageLogId;
	@Column(name = "MESSAGE_PAYLOAD")
	private String messagePayLoad;
	@Column(name = "MESSAGE_CREATED_BY")
	private String messageCreatedBy;
	@Column(name = "MESSAGE_CREATED_ON")
	private Date messageCreatedOn;
	
	
	public Long getMessageLogId() {
		return messageLogId;
	}
	
	public void setMessageLogId(Long messageLogId) {
		this.messageLogId = messageLogId;
	}
	
	public String getMessagePayLoad() {
		return messagePayLoad;
	}
	
	public void setMessagePayLoad(String messagePayLoad) {
		this.messagePayLoad = messagePayLoad;
	}
	
	public String getMessageCreatedBy() {
		return messageCreatedBy;
	}
	
	public void setMessageCreatedBy(String messageCreatedBy) {
		this.messageCreatedBy = messageCreatedBy;
	}
	
	public Date getMessageCreatedOn() {
		return messageCreatedOn;
	}
	
	public void setMessageCreatedOn(Date messageCreatedOn) {
		this.messageCreatedOn = messageCreatedOn;
	}
	
	
	
	
	

}
