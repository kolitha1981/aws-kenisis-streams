package com.amazonaws.lambda.kenesis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaFunctionHandler implements RequestStreamHandler {

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

		int letter = 0;
		while ((letter = input.read()) >= 0) {
			output.write(Character.toUpperCase(letter));
		}
	}

}
