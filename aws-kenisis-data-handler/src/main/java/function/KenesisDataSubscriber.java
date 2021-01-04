package function;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.amazonaws.lambda.kenesis.constants.EnvironmentConstants;
import com.amazonaws.lambda.kenesis.constants.WebConstants;
import com.amazonaws.lambda.kenesis.httpclient.HttpConnectionKeepAliveStrategy;
import com.amazonaws.lambda.kenesis.model.Message;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.google.gson.Gson;

public class KenesisDataSubscriber implements RequestHandler<KinesisEvent, Integer> {

	private static PoolingHttpClientConnectionManager connectionManager;
	private static HttpConnectionKeepAliveStrategy connectionKeepAliveStrategy;
	static {
		final String endpointHost = System.getenv(EnvironmentConstants.ENV_MESSAGE_SERVICE_ENDPOINT_HOST);
		final int routePort = Integer.parseInt(System.getenv(EnvironmentConstants.ENV_MESSAGE_SERVICE_ENDPOINT_PORT));
		final HttpHost endPointHost = new HttpHost(endpointHost, routePort);
		final HttpRoute endpointRoute = new HttpRoute(endPointHost);
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(WebConstants.NUMBER_OF_MAX_CONNECTIONS_PER_ROUTE);
		connectionManager.setMaxTotal(WebConstants.TOTAL_NUMBER_OF_CONNECTIONS);
		connectionManager.setSocketConfig(endPointHost,
				SocketConfig.custom().setSoTimeout(WebConstants.HTTP_SOCKET_CONNECTION_TIMEOUT_IN_MILLS).build());
		connectionManager.setMaxPerRoute(endpointRoute, WebConstants.NUMBER_OF_MAX_CONNECTIONS_PER_ROUTE);
		connectionKeepAliveStrategy = new HttpConnectionKeepAliveStrategy();
	}

	@Override
	public Integer handleRequest(KinesisEvent event, Context context) {
		final LambdaLogger lambdaLogger = context.getLogger();
		lambdaLogger.log("@@@@@@@@KinesisEvent: " + new Gson().toJson(event));
		final List<Message> messagesToBeSaved = new ArrayList<>();
		lambdaLogger.log("@@@@@@@@Started executing the function handleRequest() ........ ");
		for (KinesisEventRecord record : event.getRecords()) {
			String payload = String.valueOf(record.getKinesis().getData().array());
			lambdaLogger.log("@@@@@@@@Payload: " + payload);
			messagesToBeSaved.add(new Gson().fromJson(payload, Message.class));
		}
		return postMessages(messagesToBeSaved, lambdaLogger);
	}

	private int postMessages(List<Message> messages, LambdaLogger lambdaLogger) {
		try (CloseableHttpClient client = HttpClients.custom().setKeepAliveStrategy(connectionKeepAliveStrategy)
				.setConnectionManager(connectionManager).build();) {
			HttpPost httpPost = new HttpPost(EnvironmentConstants.ENV_MESSAGE_SERVICE_ENDPOINT_HOST);
			lambdaLogger.log(
					"@@@@@@@@Making the request to endpint: " + EnvironmentConstants.ENV_MESSAGE_SERVICE_ENDPOINT_HOST);
			httpPost.setEntity(new StringEntity(new Gson().toJson(messages)));
			httpPost.setHeader(WebConstants.HEADER_NAME_ACCEPT_KEY, WebConstants.HEADER_NAME_ACCEPT_VALUE);
			httpPost.setHeader(WebConstants.HEADER_NAME_CONTENT_TYPE_KEY, WebConstants.HEADER_NAME_CONTENT_TYPE_VALUE);
			CloseableHttpResponse response = client.execute(httpPost);
			lambdaLogger.log("@@@@@@@@Response: " + new Gson().toJson(response));
			int httpStatusCode = response.getStatusLine().getStatusCode();
			lambdaLogger.log("@@@@@@@@StatusCode: " + httpStatusCode);
			final String messageidString = String.join(",",
					messages.stream().map(message -> message.getMessageId().toString()).collect(Collectors.toList()));
			if (httpStatusCode == HttpStatus.SC_OK) {
				lambdaLogger.log("Successfully saved messageIds:" + messageidString);
				return WebConstants.FUNCTION_STATUS_OK;
			}
			lambdaLogger.log("Error saving messageIds:" + messageidString);
		} catch (Exception e) {
			lambdaLogger.log("Error saving messageIds:" + e.getMessage());
		}
		return WebConstants.FUNCTION_STATUS_ERROR;
	}
}
