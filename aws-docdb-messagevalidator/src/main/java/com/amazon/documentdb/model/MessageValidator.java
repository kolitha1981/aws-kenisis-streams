package com.amazon.documentdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_VALIDATOR")
public class MessageValidator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VALIDATOR_ID")
	private Long messageValidatorId;
	@Column(name = "PAYLOAD_MAX_CHAR_LIMIT")
	private int payLoadMaxCharLimit;
	@Column(name = "VALIDATOR_TYPE")
	@Enumerated(EnumType.ORDINAL)
	private ValidatorType validatorType;
	
	
	public Long getMessageValidatorId() {
		return messageValidatorId;
	}
	
	public void setMessageValidatorId(Long messageValidatorId) {
		this.messageValidatorId = messageValidatorId;
	}
	
	public int getPayLoadMaxCharLimit() {
		return payLoadMaxCharLimit;
	}
	
	public void setPayLoadMaxCharLimit(int payLoadMaxCharLimit) {
		this.payLoadMaxCharLimit = payLoadMaxCharLimit;
	}
	
	public ValidatorType getValidatorType() {
		return validatorType;
	}
	
	public void setValidatorType(ValidatorType validatorType) {
		this.validatorType = validatorType;
	}	

}
