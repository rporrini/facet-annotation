package it.disco.unimib.labeller.labelling;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpConnector{
	
	public String get(String httpRequest) throws Exception{
		return IOUtils.toString(new DefaultHttpClient().execute(new HttpGet(httpRequest)).getEntity().getContent());
	}
}