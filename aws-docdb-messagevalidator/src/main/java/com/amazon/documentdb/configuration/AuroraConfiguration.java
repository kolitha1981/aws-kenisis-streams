package com.amazon.documentdb.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.amazon.documentdb.repository")
public class AuroraConfiguration {
	
	

}
