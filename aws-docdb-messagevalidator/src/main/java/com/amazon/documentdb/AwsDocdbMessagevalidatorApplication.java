package com.amazon.documentdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.amazon.documentdb")
public class AwsDocdbMessagevalidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsDocdbMessagevalidatorApplication.class, args);
	}

}
