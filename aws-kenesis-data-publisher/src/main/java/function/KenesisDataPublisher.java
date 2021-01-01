package function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.lambda.constants.ApplicationConstants;
import com.amazonaws.lambda.constants.EnvironmentConstants;
import com.amazonaws.lambda.constants.WebConstants;
import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class KenesisDataPublisher implements RequestStreamHandler {

	private static String kenesisStreamName;
	private static AmazonKinesis amazonKinesisClient;

	static {
		kenesisStreamName = System.getenv(EnvironmentConstants.ENV_KENESIS_STREAM_NAME);
		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		clientBuilder.setRegion("us-east-1");
		amazonKinesisClient = clientBuilder.build();
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		final LambdaLogger lambdaLogger = context.getLogger();
		validateStream(amazonKinesisClient, kenesisStreamName, lambdaLogger);
		final PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
		putRecordsRequest.setStreamName(kenesisStreamName);
		final List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
		final JsonObject responseJson = new JsonObject();
		JsonObject headerJson = new JsonObject();
		headerJson.addProperty(WebConstants.RESPONSE_HEADER_CUSTOM_VALUE, "my custom header value");
		final JsonObject responseBody = new JsonObject();
		lambdaLogger.log("@@@@@Generating records to be pushed to the stream ....... ");
		for (int i = 0; i < 10; ++i) {
			final Message message = new Message.MessageBuilder(Long.valueOf(i), "Test payload count :" + i)
					.addCreatedBy("Admin").addCreatedOn(new Date()).build();
			final PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
			putRecordsRequestEntry.setData(ByteBuffer.wrap(message.toJson().getBytes()));
			putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
			putRecordsRequestEntryList.add(putRecordsRequestEntry);
		}
		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		lambdaLogger.log("@@@@@Called records putRecordsRequest.setRecords() on the set of records .....");
		PutRecordsResult putRecordsResult =  null;
		try {
			putRecordsResult = amazonKinesisClient.putRecords(putRecordsRequest);
			lambdaLogger.log("@@@@@Called records putRecordsRequest.putRecords() on the set of records .....");
		}catch(Exception e)
		{
			lambdaLogger.log("Error sending record to kenesis: "+ e.getMessage());
			responseBody.addProperty(WebConstants.RESPONSE_BODY_KENSIS_ERROR,
					e.getMessage());
			responseJson.addProperty(WebConstants.RESPONSE_STATUS, 500);
		}
		lambdaLogger.log("@@@@@Result:"+ putRecordsResult);
		if(putRecordsResult != null)
			if (putRecordsResult.getFailedRecordCount() <= 0) {
				responseBody.addProperty(WebConstants.RESPONSE_BODY_PARAMETER_MESSAGES_FAILED,
						Collections.emptyList().toString());
				responseJson.addProperty(WebConstants.RESPONSE_STATUS, 200);
			} else {
				lambdaLogger.log("@@@@@Failed records are existing");
				responseBody.addProperty(WebConstants.RESPONSE_BODY_PARAMETER_MESSAGES_FAILED,
						String.join(",", putRecordsResult.getRecords().stream()
								.filter(putRecordsResultEntry -> putRecordsResultEntry.getErrorCode() != null)
								.map(putRecordsResultEntry -> putRecordsResultEntry.getSequenceNumber())
								.collect(Collectors.toList())));
				responseJson.addProperty(WebConstants.RESPONSE_STATUS, 500);
			}
		responseJson.add(WebConstants.RESPONSE_HEADERS, headerJson);
		responseJson.addProperty(WebConstants.RESPONSE_BODY, responseBody.toString());
		final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		writer.write(new Gson().toJson(responseJson));
		writer.close();
	}

	private void validateStream(AmazonKinesis kinesisClient, String streamName, LambdaLogger lambdaLogger) {
		try {
			lambdaLogger.log("@@@@@Started validating the stream ....... ");
			DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest().withStreamName(streamName);
			lambdaLogger.log("@@@@@describeStreamRequest:"+ new Gson().toJson(describeStreamRequest));
			DescribeStreamResult describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);
			lambdaLogger.log("@@@@@describeStreamResponse:"+ new Gson().toJson(describeStreamResponse));
			lambdaLogger.log("@@@@@Started validating the stream ....... ");
			if (!describeStreamResponse.getStreamDescription().getStreamStatus()
					.equals(ApplicationConstants.KENSIS_STAREAM_ACTIVE_STATUS)) {
				lambdaLogger.log("Stream " + streamName + " is not active. Please wait a few moments and try again.");
				System.exit(1);
			}
			lambdaLogger.log("@@@@@Ended validating the stream ....... ");
		} catch (Exception e) {
			lambdaLogger.log("Error found while describing the stream " + e.getMessage());
			System.exit(1);
		}
	}

}
