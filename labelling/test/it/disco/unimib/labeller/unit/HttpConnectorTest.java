package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.HttpConnector;

import org.junit.Test;

public class HttpConnectorTest {

	@Test
	public void shouldPerformARealRequest() throws Exception {
		String response = new HttpConnector().get("http://www.google.com");
		
		assertThat(response, is(notNullValue()));
	}
}
