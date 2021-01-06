package com.lambda.mongodb.mongodbpersister.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lambda.mongodb.mongodbpersister.config.MessageRepositoryConfig;
import com.lambda.mongodb.mongodbpersister.constants.EnvironmentConstants;
import com.lambda.mongodb.mongodbpersister.dao.MessageRepository;
import com.lambda.mongodb.mongodbpersister.exception.MessageNotFoundException;
import com.lambda.mongodb.mongodbpersister.model.Message;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@DataMongoTest(excludeAutoConfiguration = { EmbeddedMongoAutoConfiguration.class })
@ExtendWith(SpringExtension.class)
public class MessageServiceTest {

	@Autowired
	private MessageService messageService;

	@Test
	public void testSavedMessage() {
		Message messageOne = new Message(1L, "Test1", new Date(), "Admin"),
				messageTwo = new Message(2L, "Test2", new Date(), "Admin"),
				messageThree = new Message(3L, "Test3", new Date(), "Admin");
		final List<Message> messages = new ArrayList<>();
		messages.add(messageOne);
		messages.add(messageTwo);
		messages.add(messageThree);
		this.messageService.save(messages);
		assert this.messageService.getById(1L) != null;
		assert this.messageService.getById(2L) != null;
		assert this.messageService.getById(3L) != null;
	}

	@Test
	public void testUnSavedMessage() {
		Message messageOne = new Message(1L, "Test1", new Date(), "Admin"),
				messageTwo = new Message(2L, "Test2", new Date(), "Admin"),
				messageThree = new Message(3L, "Test3", new Date(), "Admin");
		final List<Message> messages = new ArrayList<>();
		messages.add(messageOne);
		messages.add(messageTwo);
		messages.add(messageThree);
		this.messageService.save(messages);
		try {
			this.messageService.getById(4L);
			fail();
		} catch (MessageNotFoundException e) {
			assertTrue(true);
		}
	}

	@Configuration
	static class MongoConfiguration implements InitializingBean, DisposableBean {
		MongodExecutable executable;
		@Value("${org.persistent.mongodb.endpoint}")
		private String server;
		@Value("${org.persistent.mongodb.port}")
		private int serverPort;

		@Override
		public void afterPropertiesSet() throws Exception {
			executable = MongodStarter.getDefaultInstance()
					.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION)
							.net(new Net(server, serverPort, Network.localhostIsIPv6())).build());
			executable.start();
		}

		@Bean
		public MongoRepositoryFactoryBean mongoFactoryRepositoryBean() {
			final MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString("mongodb://localhost:27017/messages")).build());
			final MongoTemplate template = new MongoTemplate(mongoClient, "messages");
			final MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(
					MessageRepository.class);
			mongoDbFactoryBean.setMongoOperations(template);
			return mongoDbFactoryBean;
		}
		
		@Bean
		public MessageRepositoryConfig messageRepositoryConfig()
		{
			final Environment environment = Mockito.mock(Environment.class);
			Mockito.doReturn("27107").when(environment).getProperty(EnvironmentConstants.ENV_MONGO_DB_PORT);
			Mockito.doReturn("localhost").when(environment).getProperty(EnvironmentConstants.ENV_MONGO_DB_ENDPOINT);
			Mockito.doReturn("messages").when(environment).getProperty(EnvironmentConstants.ENV_MONGO_DB_NAME);
			return new MessageRepositoryConfig(environment);
		}

		@Bean
		public MessageService messageService(MessageRepository messageRepository) {
			return new MessageServiceImpl(messageRepository);
		}

		@Override
		public void destroy() throws Exception {
			executable.stop();
		}
	}

}
