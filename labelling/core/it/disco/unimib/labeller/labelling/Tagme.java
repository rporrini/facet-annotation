package it.disco.unimib.labeller.labelling;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Tagme implements Annotator {

	private HttpConnector connector;

	public Tagme(HttpConnector connector) {
		this.connector = connector; 
	}

	@Override
	public List<String> annotate(String... instances) throws Exception {
		logRequestFor(instances);
		JsonObject result = new Gson().fromJson(connector.get(createQueryString(instances)), JsonObject.class);
		ArrayList<String> spottedInstances = new ArrayList<String>();
		for(JsonElement element : (JsonArray)result.get("annotations")){
			String spottedType = asType(element);
			log(spottedType);
			spottedInstances.add(spottedType);
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
	
	private void log(String spottedType) {
		new Events().debug("retrieved entity: " + spottedType);
	}
	
	private void logRequestFor(String... instances) {
		new Events().debug("querying tagme for: [" + StringUtils.join(instances, ", ") + "]");
	}
}
