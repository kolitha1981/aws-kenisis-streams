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

import software.amazon.awssdk.regions.Region;

public class KenesisDataPublisher implements RequestStreamHandler {

	private static String kenesisStreamName;
	private static AmazonKinesis amazonKinesisClient;

	static {
		kenesisStreamName = System.getenv(EnvironmentConstants.ENV_KENESIS_STREAM_NAME);
		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		clientBuilder.setRegion(Region.US_EAST_1.toString());
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
		for (int i = 0; i < 10; i++) {
			final Message message = new Message.MessageBuilder(Long.valueOf(i), "Test payload count :" + i)
					.addCreatedBy("Admin").addCreatedOn(new Date()).build();
			final PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
			putRecordsRequestEntry.setData(ByteBuffer.wrap(message.toJson().getBytes()));
			putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
			putRecordsRequestEntryList.add(putRecordsRequestEntry);
		}
		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		PutRecordsResult putRecordsResult = amazonKinesisClient.putRecords(putRecordsRequest);
		if (putRecordsResult.getFailedRecordCount() > 0) {
			final List<String> errorSequenceNumbers = putRecordsResult.getRecords().stream()
					.filter(putRecordsResultEntry -> putRecordsResultEntry.getErrorCode() != null)
					.map(putRecordsResultEntry -> putRecordsResultEntry.getSequenceNumber())
					.collect(Collectors.toList());
			responseBody.addProperty(WebConstants.RESPONSE_BODY_PARAMETER_MESSAGES_FAILED,
					String.join(",", errorSequenceNumbers));
			responseJson.addProperty(WebConstants.RESPONSE_STATUS, 500);
		} else {
			responseBody.addProperty(WebConstants.RESPONSE_BODY_PARAMETER_MESSAGES_FAILED,
					Collections.emptyList().toString());
			responseJson.addProperty(WebConstants.RESPONSE_STATUS, 200);
		}
		responseJson.add(WebConstants.RESPONSE_HEADERS, headerJson);
		responseJson.addProperty(WebConstants.RESPONSE_BODY, responseBody.toString());
		final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		writer.write(new Gson().toJson(responseJson));
		writer.close();
	}

	private void validateStream(AmazonKinesis kinesisClient, String streamName, LambdaLogger lambdaLogger) {
		try {
			DescribeStreamRequest describeStreamRequest = new DescribeStreamRequest().withStreamName(streamName);
			DescribeStreamResult describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);
			if (!describeStreamResponse.getStreamDescription().getStreamStatus()
					.equals(ApplicationConstants.KENSIS_STAREAM_ACTIVE_STATUS)) {
				lambdaLogger.log("Stream " + streamName + " is not active. Please wait a few moments and try again.");
				System.exit(1);
			}
		} catch (Exception e) {
			lambdaLogger.log("Error found while describing the stream " + streamName);
			System.exit(1);
		}
	}

}
