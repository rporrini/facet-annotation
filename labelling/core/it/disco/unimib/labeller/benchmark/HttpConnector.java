package it.disco.unimib.labeller.benchmark;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpConnector{
	
	public String get(String httpRequest) throws Exception{
		return IOUtils.toString(HttpClientBuilder.create().build().execute(new HttpGet(httpRequest)).getEntity().getContent());
	}
}