package function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class KenesisDataSubscriber implements RequestHandler<KinesisEvent, String> {

	private MongoDbRepository mongoDbRepository;

	public KenesisDataSubscriber() {
		this.mongoDbRepository = new MongoDbRepositoryImpl();
	}

	@Override
	public String handleRequest(KinesisEvent event, Context context) {
		final LambdaLogger lambdaLogger = context.getLogger();
		final List<Message> messagesToBeSaved = new ArrayList<Message>();
		lambdaLogger.log("@@@@@@@@Started executing the function handleRequest() ........ ");
		for (KinesisEventRecord record : event.getRecords()) {
			String payload = String.valueOf(Base64.decode(record.getKinesis().getData().array()));
			lambdaLogger.log("@@@@@@@@Payload: " + payload);
			messagesToBeSaved.add(new Gson().fromJson(payload, Message.class));
		}
		final List<Message> savedMessages = this.mongoDbRepository.save(messagesToBeSaved, lambdaLogger);
		final String messageIds = String.join(",", savedMessages.stream().map(message -> message.getMessageId().toString()).collect(Collectors.toList()));
		lambdaLogger.log("@@@@@@@@Saved messages: " + messageIds);
		return messageIds;
	}
}
