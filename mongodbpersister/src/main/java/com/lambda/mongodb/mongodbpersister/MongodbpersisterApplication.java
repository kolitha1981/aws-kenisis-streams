package com.lambda.mongodb.mongodbpersister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.lambda.mongodb.mongodbpersister")
@SpringBootApplication
public class MongodbpersisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongodbpersisterApplication.class, args);
	}

}
