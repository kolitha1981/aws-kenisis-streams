package function;

import com.amazonaws.lambda.rds.MongoDBRepository;
import com.amazonaws.lambda.rds.MongoDBRepositoryImpl;
import com.amazonaws.lambda.rds.model.Message;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.google.gson.Gson;

public class RDSEventHandler implements RequestHandler<SNSEvent, String> {

	private MongoDBRepository mongoDBRepository;

	public RDSEventHandler() {
		this.mongoDBRepository = new MongoDBRepositoryImpl();
	}

	@Override
	public String handleRequest(SNSEvent event, Context context) {
		final LambdaLogger lambdaLogger = context.getLogger();
		lambdaLogger.log("@@@@@@@Handling SNS event ........");
		if (event.getRecords() == null || event.getRecords().isEmpty())
			return null;
		final SNS snsMessage = event.getRecords().get(0).getSNS();
		if (snsMessage == null)
			return null;
		final Message message = new Gson().fromJson(snsMessage.getMessage(), Message.class);
		lambdaLogger.log("Message :" + message);
		return this.mongoDBRepository.save(message, lambdaLogger).toJson().getAsString();
	}

}
