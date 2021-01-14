package com.amazon.documentdb.service;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazon.documentdb.AwsDocdbMessagevalidatorApplication;
import com.amazon.documentdb.model.Message;
import com.amazon.documentdb.model.MessageLog;

@SpringBootTest(classes = AwsDocdbMessagevalidatorApplication.class)
public class MessageLoggerServiceImplTest {
	
	@Autowired
	private MessageLoggerService loggerService;
	
	@Test
	public void testSaveLog() {
		final Message message =  new Message(1L,"Test Payload ....", new Date(),
				"Adminstrator");
		final MessageLog savedLog = this.loggerService.save(message);
		assertNotNull(savedLog);
		assertNotNull(savedLog.getMessageLogId());
	}

}
