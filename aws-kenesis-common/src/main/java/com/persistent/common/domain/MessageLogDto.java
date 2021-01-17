package com.persistent.common.domain;

import java.util.Date;

public class MessageLogDto {
	
	private Long messageLogId;
	private String messagePayLoad;
	private String messageCreatedBy;
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
