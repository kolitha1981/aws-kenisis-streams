package com.amazonaws.lambda.rds;

import com.amazonaws.lambda.rds.model.Message;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public interface MongoDBRepository {

	Message save(Message message, LambdaLogger lambdaLogger);

}
