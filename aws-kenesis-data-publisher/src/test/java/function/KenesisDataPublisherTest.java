package function;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import function.KenesisDataPublisher;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class KenesisDataPublisherTest {

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testLambdaFunctionHandler() throws IOException {
		KenesisDataPublisher handler = new KenesisDataPublisher();
		InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		handler.handleRequest(input, output, null);
		String sampleOutputString = output.toString();
		System.out.println(sampleOutputString);
		Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
	}
}
