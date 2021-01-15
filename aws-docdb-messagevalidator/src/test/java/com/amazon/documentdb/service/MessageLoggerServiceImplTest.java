package com.amazon.documentdb.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
		final Message message =  new Message(null,"Test Payload ....", new Date(),
				"Adminstrator");
		final MessageLog savedLog = this.loggerService.save(message);
		assertNotNull(savedLog);
		assertNotNull(savedLog.getMessageLogId());
	}
	
	@Test
	public void testSaveLogs() {
		final Message messageOne =  new Message(null,"Test Payload One....", new Date(),
				"Adminstrator");
		final Message messageTwo =  new Message(null,"Test Payload Two....", new Date(),
				"Adminstrator");
		final Message messageThree =  new Message(null,"Test Payload Three....", new Date(),
				"Adminstrator");
		final List<Message> messages =  new ArrayList<>();
		messages.add(messageOne);
		messages.add(messageTwo);
		messages.add(messageThree);
		final List<MessageLog> savedLogs = this.loggerService.save(messages);
		assertNotNull(savedLogs);
		final List<Long> savedLogIds = savedLogs.stream().map((log) -> {return log.getMessageLogId();})
		   .collect(Collectors.toList());
		assertNotNull(savedLogIds);
		assertTrue(savedLogIds.size() == 3);
	}

}
