package it.disco.unimib.test;

import it.disco.unimib.labelling.HttpConnector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HttpConnectorTestDouble extends HttpConnector{
	
	JsonObject response;
	String lastRequest;
	
	public HttpConnectorTestDouble(){
		response = new JsonObject();
		response.add("annotations", new JsonArray());
	}
	
	@Override
	public String get(String httpRequest) {
		lastRequest = httpRequest;
		return response.toString();
	}

	public HttpConnectorTestDouble thatReturns(String type) {
		JsonObject typeObject = new JsonObject();
		typeObject.addProperty("title", type);
		((JsonArray)response.get("annotations")).add(typeObject);
		return this;
	}

	public String lastRequest() {
		return lastRequest;
	}
}