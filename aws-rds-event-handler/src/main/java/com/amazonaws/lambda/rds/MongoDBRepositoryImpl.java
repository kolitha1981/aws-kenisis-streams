package com.amazonaws.lambda.rds;

import com.amazonaws.lambda.rds.constants.EnvironmentConstants;
import com.amazonaws.lambda.rds.exception.MongoDBException;
import com.amazonaws.lambda.rds.model.Message;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

public class MongoDBRepositoryImpl implements MongoDBRepository {

	private static MongoClient mongoClient;

	static {
		mongoClient = new MongoClient(new MongoClientURI(
				String.format("mongodb://%s:%s@%s", System.getenv(EnvironmentConstants.ENV_MONGO_DB_USERNAME).trim(),
						System.getenv(EnvironmentConstants.ENV_MONGO_DB_PASSWORD).trim(),
						System.getenv(EnvironmentConstants.ENV_MONGO_DB_ENDPOINT).trim() + ":"
								+ System.getenv(EnvironmentConstants.ENV_MONGO_DB_PORT).trim())
						.trim(),
				MongoClientOptions.builder().connectTimeout(3000)));
	}

	@Override
	public Message save(Message message, LambdaLogger lambdaLogger) {
		lambdaLogger.log("@@@@Calling save()...........");
		try {
			mongoClient.getDatabase(System.getenv(EnvironmentConstants.ENV_MONGO_DB_NAME).trim())
					.getCollection(System.getenv(EnvironmentConstants.ENV_MONGO_DB_COLLECTION_NAME).trim()).insertOne(message.toDocument());
			return message;
		} catch (Exception e) {
			lambdaLogger.log("@@@@Exception when saving message :" + e.getMessage());
			throw new MongoDBException(e.getMessage(), e);
		}
	}

}
