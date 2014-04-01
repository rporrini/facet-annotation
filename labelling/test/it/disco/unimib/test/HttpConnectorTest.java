package it.disco.unimib.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labelling.HttpConnector;

import org.junit.Test;

public class HttpConnectorTest {

	@Test
	public void shouldPerformARealRequest() throws Exception {
		String response = new HttpConnector().get("http://www.google.com");
		
		assertThat(response, is(notNullValue()));
	}
}
