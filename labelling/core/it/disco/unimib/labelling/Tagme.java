package it.disco.unimib.labelling;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tagme {

	private HttpConnector connector;

	public Tagme(HttpConnector connector) {
		this.connector = connector; 
	}

	public List<String> annotate(String... instances) throws Exception {
		JsonObject result = new Gson().fromJson(connector.get(createQueryString(instances)), JsonObject.class);
		ArrayList<String> spottedInstances = new ArrayList<String>();
		for(JsonElement element : (JsonArray)result.get("annotations")){
			spottedInstances.add(asType(element));
		}
		return spottedInstances;
	}

	private String createQueryString(String... instances) throws Exception {
		return "http://tagme.di.unipi.it/tag?text=" +
			   URLEncoder.encode(StringUtils.join(instances, ", "), "UTF-8") +
			   "&key=ricpor2014&lang=en&include_categories=true";
	}

	private String asType(JsonElement element) {
		return "http://dbpedia.org/resource/" + element.getAsJsonObject().get("title").getAsString().replace(" ", "_");
	}
}
