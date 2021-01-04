package com.amazonaws.lambda.kenesis.httpclient;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.amazonaws.lambda.kenesis.constants.WebConstants;

public class HttpConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	@Override
	public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		for (final HeaderElementIterator headerIterator = new BasicHeaderElementIterator(
				response.headerIterator(HTTP.CONN_KEEP_ALIVE)); headerIterator.hasNext();) {
			HeaderElement headerElement = headerIterator.nextElement();
			String value = headerElement.getValue();
			if (value != null && "timeout".equalsIgnoreCase(headerElement.getName()))
				return 1000 * Long.parseLong(value);
		}
		return (long)(1000 * WebConstants.DEFAULT_CONNECTION_KEEP_ALIVE_DURATION_IN_SECS);
	}

}
