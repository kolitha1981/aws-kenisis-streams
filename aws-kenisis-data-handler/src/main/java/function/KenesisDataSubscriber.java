package function;

import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.amazonaws.util.Base64;
import com.google.gson.Gson;
import com.persisstent.event.repository.MongoDbRepository;
import com.persisstent.event.repository.MongoDbRepositoryImpl;

public class KenesisDataSubscriber implements RequestHandler<KinesisEvent, Integer> {

	private MongoDbRepository mongoDbRepository;

	public KenesisDataSubscriber() {
		this.mongoDbRepository = new MongoDbRepositoryImpl();
	}

	@Override
	public Integer handleRequest(KinesisEvent event, Context context) {
		final LambdaLogger lambdaLogger = context.getLogger();
		Message savedMessage = null;
		lambdaLogger.log("@@@@@@@@Started executing the function handleRequest() ........ ");
		for (KinesisEventRecord record : event.getRecords()) {
			String payload = String.valueOf(Base64.decode(record.getKinesis().getData().array()));
			lambdaLogger.log("@@@@@@@@Payload: " + payload);
			savedMessage = this.mongoDbRepository.save(new Gson().fromJson(payload, Message.class), lambdaLogger);
			lambdaLogger.log("@@@@@@@@Saved message: " + savedMessage);
		}
		return savedMessage == null ? 0 : savedMessage.getMessageId().intValue();
	}
}
