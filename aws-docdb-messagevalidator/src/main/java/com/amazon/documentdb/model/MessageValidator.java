package com.amazon.documentdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MessageValidator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VALIDATOR_ID")
	private Long messageValidatorid;
	@Column(name = "VALIDATOR_XML")
	private String validatorXml;
	@Column(name = "VALIDATOR_TYPE")
	private ValidatorType validatorType;
	
	
	public Long getMessageValidatorid() {
		return messageValidatorid;
	}
	public void setMessageValidatorid(Long messageValidatorid) {
		this.messageValidatorid = messageValidatorid;
	}
	public String getValidatorXml() {
		return validatorXml;
	}
	public void setValidatorXml(String validatorXml) {
		this.validatorXml = validatorXml;
	}
	public ValidatorType getValidatorType() {
		return validatorType;
	}
	public void setValidatorType(ValidatorType validatorType) {
		this.validatorType = validatorType;
	}
	
	
	

}
