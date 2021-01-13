package com.amazon.documentdb.model;

public enum ValidatorType {

	VALIDATOR_TYPE_SIMPLE(1), VALIDATOR_TYPE_COMPLEX(2);

	private int validatorType;

	private ValidatorType(int validatorType) {
		this.validatorType = validatorType;
	}

	public int getValidatorType() {
		return validatorType;
	}

}
