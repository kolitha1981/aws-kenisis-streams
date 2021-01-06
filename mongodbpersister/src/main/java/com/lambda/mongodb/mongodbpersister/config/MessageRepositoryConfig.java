package com.lambda.mongodb.mongodbpersister.config;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.lambda.mongodb.mongodbpersister.constants.EnvironmentConstants;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
@Configuration
@EnableMongoRepositories(basePackages = "com.lambda.mongodb.mongodbpersister.dao")
public class MessageRepositoryConfig extends AbstractMongoClientConfiguration {

	private String databaseName;
	@Value("${org.persistent.mongodb.endpoint}")
	private String server;
	private String serverPort;
	
	@Autowired	
	public MessageRepositoryConfig(Environment environment) {
		this.databaseName = environment.getProperty(EnvironmentConstants.ENV_MONGO_DB_NAME);
		this.server = environment.getProperty(EnvironmentConstants.ENV_MONGO_DB_ENDPOINT);
		this.serverPort = environment.getProperty(EnvironmentConstants.ENV_MONGO_DB_PORT);
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
    public MongoClient mongoClient() {
    	return MongoClients.create(MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(
						MessageFormat.format("mongodb://{0}:{1}/{2}", 
								new Object[] { server, serverPort, databaseName })))
				.build());
    }
	
}
