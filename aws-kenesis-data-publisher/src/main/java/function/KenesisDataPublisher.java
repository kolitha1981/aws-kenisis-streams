package function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.lambda.constants.ApplicationConstants;
import com.amazonaws.lambda.constants.EnvironmentConstants;
import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

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
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		final LambdaLogger lambdaLogger = context.getLogger();
		validateStream(amazonKinesisClient, kenesisStreamName, lambdaLogger);
		final PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
		putRecordsRequest.setStreamName(kenesisStreamName);
		final List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final Message message = new Message.MessageBuilder(Long.valueOf(i), "Test payload count :" + i)
					.addCreatedBy("Admin").addCreatedOn(new Date()).build();
			final PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
			putRecordsRequestEntry.setData(ByteBuffer.wrap(message.toJson().getBytes()));
			putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
			putRecordsRequestEntryList.add(putRecordsRequestEntry);
		}
		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		amazonKinesisClient.putRecords(putRecordsRequest);
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
