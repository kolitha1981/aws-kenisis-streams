package function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class KenesisDataPublisher implements RequestStreamHandler {

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		final LambdaLogger lambdaLogger =  context.getLogger();
		for (int j = 0; j < 30; j++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				lambdaLogger.log("@@@@@InterruptedException:"+ e.getMessage());
			}
		}
	}

}
