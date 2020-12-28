package com.amazonaws.lambda.rds;

import org.bson.Document;

import com.amazonaws.lambda.rds.constants.EnvironmentConstants;
import com.amazonaws.lambda.rds.exception.MongoDBException;
import com.amazonaws.lambda.rds.model.Message;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBRepositoryImpl implements MongoDBRepository {

	private static MongoClient mongoClient;

	static {
		final String connectionString = String.format("mongodb://%s:%s@%s",
				System.getenv(EnvironmentConstants.ENV_MONGO_DB_USERNAME).trim(),
				System.getenv(EnvironmentConstants.ENV_MONGO_DB_PASSWORD).trim(),
				System.getenv(EnvironmentConstants.ENV_MONGO_DB_ENDPOINT).trim() + ":"
						+ System.getenv(EnvironmentConstants.ENV_MONGO_DB_PORT).trim());
		Builder mongoOptionsBuilder = MongoClientOptions.builder().connectTimeout(3000);
		mongoClient = new MongoClient(new MongoClientURI(connectionString.trim(), mongoOptionsBuilder));
	}

	@Override
	public Message save(Message message, LambdaLogger lambdaLogger) {
		lambdaLogger.log("@@@@Calling save()...........");
		try {
			MongoDatabase mongoDatabase = mongoClient
					.getDatabase(System.getenv(EnvironmentConstants.ENV_MONGO_DB_NAME).trim());
			MongoCollection<Document> colection = mongoDatabase
					.getCollection(System.getenv(EnvironmentConstants.ENV_MONGO_DB_COLLECTION_NAME).trim());
			colection.insertOne(message.toDocument());
			return message;
		} catch (Exception e) {
			lambdaLogger.log("@@@@Exception when saving message :" + e.getMessage());
			throw new MongoDBException(e.getMessage(), e);
		}
	}

}
