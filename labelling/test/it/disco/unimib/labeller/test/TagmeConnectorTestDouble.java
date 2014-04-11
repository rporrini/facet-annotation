package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.labelling.HttpConnector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TagmeConnectorTestDouble extends HttpConnector{
	
	JsonObject response;
	String lastRequest;
	
	public TagmeConnectorTestDouble(){
		response = new JsonObject();
		response.add("annotations", new JsonArray());
	}
	
	@Override
	public String get(String httpRequest) {
		lastRequest = httpRequest;
		return response.toString();
	}

	public TagmeConnectorTestDouble thatReturns(String type) {
		JsonObject typeObject = new JsonObject();
		typeObject.addProperty("title", type);
		((JsonArray)response.get("annotations")).add(typeObject);
		return this;
	}

	public String lastRequest() {
		return lastRequest;
	}
}