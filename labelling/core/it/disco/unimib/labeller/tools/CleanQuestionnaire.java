package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.HttpConnector;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CleanQuestionnaire {

	public static void main(String[] args) throws Exception {
		String uncleanedQuestionnaire = args[0];
		
		SpreadSheet document = SpreadSheet.createFromFile(new File(uncleanedQuestionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		String lastFirstCell = "";
		for(int row=4; row<=7151; row++){
			MutableCell<SpreadSheet> relevanceColumn = resultSheet.getCellAt("C" + row);
			String relevance = relevanceColumn.getTextValue();
			if(!relevance.equals("0")){
				MutableCell<SpreadSheet> property = resultSheet.getCellAt("A" + row);
				String firstCell = valueOf(property);
				Object[] rowContent = {
						firstCell,
						valueOf(resultSheet.getCellAt("B" + row)),
						relevanceValueOf(relevanceColumn),
						valueOf(resultSheet.getCellAt("D" + row)),
						valueOf(resultSheet.getCellAt("E" + row)),
				};
				List<Object> content = new ArrayList<Object>(Arrays.asList(rowContent));
				if(firstCell.startsWith("=HYPERLINK")){
					content.add(2, "");
					content.set(4, suggestionsFor(property));
					content.add(4, sampleSuggestions(property));
					if(!lastFirstCell.startsWith("=HYPERLINK")){
						rows.add("");
						rows.add("DBPEDIA PROPERTY|PROPERTY NAME|RATE THE RELEVANCE OF THE PROPERTY|IS THIS PROPERTY NAME A CORRECT LABEL? (mark with X)|EXAMPLE 1|EXAMPLE 2");
					}
				}
				if(lastFirstCell.isEmpty()){
					content.remove(2);
					rows.add("GROUP ID|CONTEXT LABEL");
				}
				try{
					if(Long.parseLong(lastFirstCell) > 10000){
						rows.add("GROUP VALUES");
					}
				}
				catch(Exception e){}
				lastFirstCell = firstCell;
				rows.add(StringUtils.join(content, "|"));
			}
		}
		String cleanedQuestionnaire = args[1];
		FileUtils.writeLines(new File(cleanedQuestionnaire), rows);
	}

	private static String sampleSuggestions(MutableCell<SpreadSheet> cell) throws Exception{
		if(cell.getTextValue().toLowerCase().contains("dbpedia") || cell.getTextValue().toLowerCase().contains("foaf")){
			JsonObject result = new Gson().fromJson(new HttpConnector().get(dbPediaQuery(cell, "application/json")), JsonObject.class);
			JsonArray bindings = result.get("results").getAsJsonObject().get("bindings").getAsJsonArray();
			if(bindings.size() > 0){
				Random random = new Random();
				return randomResult(bindings, random) + "|" + randomResult(bindings, random);
			}
			return "|";
		}
		return valueOf(cell);
	}

	private static String randomResult(JsonArray bindings, Random random) throws Exception {
		JsonObject sampledResult = bindings.get(random.nextInt((bindings.size() - 1) + 1) + 0).getAsJsonObject();
		String subject = sampledResult.get("subject").getAsJsonObject().get("value").getAsString();
		String preposition = subject.startsWith("a") || subject.startsWith("e") || subject.startsWith("i") || subject.startsWith("o") || subject.startsWith("u") || subject.startsWith("h") ? "an" : "a"; 
		String suggestions = preposition + " " +
				subject +
				" has " +
				sampledResult.get("predicate").getAsJsonObject().get("value").getAsString() +
				" " +
				sampledResult.get("objectValue").getAsJsonObject().get("value").getAsString().replace("\n", "").replace("http://dbpedia.org/resource/", "").replace("_", " ");
		JsonElement object = sampledResult.get("object");
		if(object != null){
			suggestions += " (an entity of type " +object.getAsJsonObject().get("value").getAsString() + ")";
		}
		return suggestions;
	}
	
	private static String suggestionsFor(MutableCell<SpreadSheet> cell) throws Exception{
		if(cell.getTextValue().toLowerCase().contains("dbpedia") || cell.getTextValue().toLowerCase().contains("foaf")){
			return suggestions(cell);
		}
		return valueOf(cell);
	}
	
	private static String suggestions(MutableCell<SpreadSheet> cell) throws Exception{
		HttpPost post = new HttpPost("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyAVLJRKYhgR_yxULZ2VnPWz1ZEK-WbxToQ");
		StringEntity params =new StringEntity("{\"longUrl\":\"" + dbPediaQuery(cell, "text/html") + "\"}");
		post.addHeader("content-type", "application/json");
		post.setEntity(params);
		String response = StringUtils.join(IOUtils.readLines(HttpClientBuilder.create().build().execute(post).getEntity().getContent()), "");
		String shortUrl = new Gson().fromJson(response, JsonObject.class).get("id").getAsString();		
		return "=HYPERLINK(\"" + shortUrl + "\";" + "\"View More\")";
	}

	private static String dbPediaQuery(MutableCell<SpreadSheet> cell, String output) throws Exception {
		String uri = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> prefix pred: <" + cell.getTextValue() + "> " +
				   "select distinct ?subject ?predicate ?objectValue ?object where { " +
				   "pred: rdfs:label ?predicate . " + 
				   "?s pred: ?objectValue . " + 
				   "?s rdf:type ?subjectType . " + 
				   "?subjectType rdfs:label ?subject . " +
				   "optional{ " + 
				   "?objectValue rdf:type ?objectType . " + 
				   "?objectType rdfs:label ?object . " +
				   "filter(langMatches(lang(?object), \"en\"))" +
				   "} . " +
				   "filter(!regex(?objectType, \"Thing\", \"i\") && !regex(?subjectType, \"Thing\", \"i\") && langMatches(lang(?subject), \"en\") && langMatches(lang(?predicate), \"en\")) " +
				   "} LIMIT 200";
		return "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=" +
						URLEncoder.encode(uri, "UTF-8") + "&format=" +
						URLEncoder.encode(output, "UTF-8") +
						"&timeout=30000&debug=on";
	}
	
	private static Object relevanceValueOf(MutableCell<SpreadSheet> cell){
		String formula = cell.getFormula();
		if(formula != null && formula.startsWith("=HYPERLINK")){
			return formula;
		}
		try{
			double value = Double.parseDouble(cell.getTextValue());
			if(value <= 2.0){
				return "";
			}
		}
		catch(Exception e){
			return cell.getTextValue(); 
		}
		return cell.getTextValue();
	}
	
	private static String valueOf(MutableCell<SpreadSheet> cell){
		return cell.getFormula() == null ? cell.getTextValue() : cell.getFormula(); 
	}
}
