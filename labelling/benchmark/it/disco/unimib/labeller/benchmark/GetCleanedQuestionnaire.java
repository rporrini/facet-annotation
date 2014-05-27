package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class GetCleanedQuestionnaire {

	public static void main(String[] args) throws Exception {
		String uncleanedQuestionnaire = args[0];
		
		SpreadSheet document = SpreadSheet.createFromFile(new File(uncleanedQuestionnaire));
		Sheet resultSheet = document.getSheet(document.getSheetCount() - 1);
		
		List<String> rows = new ArrayList<String>();
		String lastFirstCell = "";
		for(int row=2; row<=7151; row++){
			MutableCell<SpreadSheet> relevanceColumn = resultSheet.getCellAt("C" + row);
			String relevance = relevanceColumn.getTextValue();
			if(!relevance.equals("0")){
				String firstCell = valueOf(resultSheet.getCellAt("A" + row));
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
					content.set(4, suggestionsFor(resultSheet.getCellAt("A" + row)));
					if(!lastFirstCell.startsWith("=HYPERLINK")){
						rows.add("PROPERTY|LABEL|RELEVANCE VALUE|CORRECT LABEL (mark with X when correct)|EXAMPLES");
					}
				}
				lastFirstCell = firstCell;
				rows.add(StringUtils.join(content, "|"));
			}
		}
		String cleanedQuestionnaire = args[1];
		FileUtils.writeLines(new File(cleanedQuestionnaire), rows);
	}

	private static String suggestionsFor(MutableCell<SpreadSheet> cell) throws Exception{
		if(cell.getTextValue().toLowerCase().contains("dbpedia")){
			return suggestions(cell);
		}
		return valueOf(cell);
	}
	
	private static String suggestions(MutableCell<SpreadSheet> cell) throws Exception{
		return "=HYPERLINK(\"" + dbPediaQuery(cell, "text/html") + "\"," + "\"Hints\")";
	}

	private static String dbPediaQuery(MutableCell<SpreadSheet> cell, String output) throws Exception {
		String uri = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
				   "select distinct ?subject ?predicate ?objectValue ?object where { " +
				   "<" + cell.getTextValue() + "> rdfs:label ?predicate . " + 
				   "?s <" + cell.getTextValue() + "> ?objectValue . " + 
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
