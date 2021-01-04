package com.amazonaws.lambda.kenesis.constants;

public class WebConstants {
	
	private WebConstants() {}
	
	public static final String HEADER_NAME_ACCEPT_KEY = "Accept";
	public static final String HEADER_NAME_ACCEPT_VALUE = "application/json";
	public static final String HEADER_NAME_CONTENT_TYPE_KEY = "Content-type";
	public static final String HEADER_NAME_CONTENT_TYPE_VALUE = "application/json";
	
	public static final int FUNCTION_STATUS_OK = 200;
	public static final int FUNCTION_STATUS_ERROR = 500;
	public static final int NUMBER_OF_MAX_CONNECTIONS_PER_ROUTE = 10;
	public static final int TOTAL_NUMBER_OF_CONNECTIONS = 10;
	public static final int DEFAULT_CONNECTION_KEEP_ALIVE_DURATION_IN_SECS = 5;
	public static final int HTTP_SOCKET_CONNECTION_TIMEOUT_IN_MILLS = 5000;

}
