package com.lambda.mongodb.mongodbpersister.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lambda.mongodb.mongodbpersister.model.Message;

public interface MessageRepository extends MongoRepository<Message, Long>{
	
}
