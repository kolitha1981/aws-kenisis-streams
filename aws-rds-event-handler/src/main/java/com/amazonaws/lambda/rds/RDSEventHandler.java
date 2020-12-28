package com.amazonaws.lambda.rds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;

public class RDSEventHandler implements RequestHandler<SNSEvent, String> {

	@Override
	public String handleRequest(SNSEvent event, Context context) {
		final LambdaLogger lambdaLogger = context.getLogger();
		lambdaLogger.log("@@@@@@@Handling SNS event ........");
		if (event.getRecords() != null && !event.getRecords().isEmpty()) {
			final SNS snsMessage = event.getRecords().get(0).getSNS();
			if (snsMessage != null) {
				String message = snsMessage.getMessage();
				lambdaLogger.log("@@@Message:" + message);
			}
		}
		return null;
	}

}
