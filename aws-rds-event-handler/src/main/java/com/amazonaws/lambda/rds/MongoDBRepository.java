package com.amazonaws.lambda.rds;

import com.amazonaws.lambda.rds.model.Message;

public interface MongoDBRepository {

	Message save(Message message);

}
