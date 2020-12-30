package com.persisstent.event.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.amazonaws.lambda.kenesis.constants.EnvironmentConstants;
import com.amazonaws.lambda.kenesis.exceptions.MongoDBException;
import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

public class MongoDbRepositoryImpl implements MongoDbRepository {

	private LambdaLogger lambdaLogger;
	private static MongoClient mongoClient;
	private static String mongoDBName;
	private static String mongoDBCollectionName;

	static {
		mongoDBName = System.getenv(EnvironmentConstants.ENV_MONGODB_DATABASENAME).trim();
		mongoDBCollectionName = System.getenv(EnvironmentConstants.ENV_MONGODB_COLLECTION_NAME).trim();
		final String connectionString = String.format("mongodb://%s:%s@%s",
				System.getenv(EnvironmentConstants.ENV_MONGODB_USERNAME).trim(),
				System.getenv(EnvironmentConstants.ENV_MONGODB_PASSWORD).trim(),
				System.getenv(EnvironmentConstants.ENV_MONGODB_ENDPOINT).trim() + ":"
						+ System.getenv(EnvironmentConstants.ENV_MONGODB_PORT).trim());
		mongoClient = new MongoClient(
				new MongoClientURI(connectionString.trim(), MongoClientOptions.builder().connectTimeout(3000)));
	}

	@Override
	public Message save(Message message, LambdaLogger lambdaLogger) {
		this.lambdaLogger.log("@@@@Calling save()...........");
		try {
			MongoCollection<Document> colection = mongoClient.getDatabase(mongoDBName).getCollection(mongoDBCollectionName);
			colection.insertOne(message.toDocument());
			return message;
		} catch (Exception e) {
			this.lambdaLogger.log("@@@@Exception when saving message :" + e.getMessage());
			throw new MongoDBException(e.getMessage(), e);
		}
	}

	@Override
	public List<Message> save(List<Message> messages, LambdaLogger lambdaLogger) {
		final List<Document> documents = messages.stream().map(message -> message.toDocument())
				.collect(Collectors.toList());
		try {
			mongoClient.getDatabase(mongoDBName).getCollection(mongoDBCollectionName)
					.insertMany(documents);
			return messages;
		} catch (Exception e) {
			this.lambdaLogger.log("@@@@Exception when saving the list of messages :" + e.getMessage());
			throw new MongoDBException(e.getMessage(), e);
		}
	}

	@Override
	public Message getById(Long messageId, LambdaLogger lambdaLogger) {
		this.lambdaLogger.log("@@@@Calling getById()...........");
		try {
			MongoCollection<Document> messageDocuments = mongoClient.getDatabase(mongoDBName)
					.getCollection(mongoDBCollectionName);
			final BasicDBObject filter = new BasicDBObject();
			filter.put("messageId", messageId);
			final Document messageDocument = messageDocuments.find(filter).first();
			return new Message(messageDocument.get("messageId", Long.class),
					messageDocument.get("payload", String.class), messageDocument.get("createdOn", Date.class),
					messageDocument.get("createdBy", String.class));
		} catch (Exception e) {
			this.lambdaLogger.log("@@@@Exception when retrieving message :" + e.getMessage());
			throw new MongoDBException(e.getMessage(), e);
		}
	}

}
