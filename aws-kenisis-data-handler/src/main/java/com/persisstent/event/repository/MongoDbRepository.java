package com.persisstent.event.repository;

import java.util.List;

import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public interface MongoDbRepository {

	Message save(Message message, LambdaLogger lambdaLogger);

	List<Message> save(List<Message> messages, LambdaLogger lambdaLogger);

	Message getById(Long messageId, LambdaLogger lambdaLogger);

}
