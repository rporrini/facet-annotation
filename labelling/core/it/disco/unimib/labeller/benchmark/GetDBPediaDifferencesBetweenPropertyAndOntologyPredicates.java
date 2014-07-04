package it.disco.unimib.labeller.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

class Predicate{
	
	public String uri;
	public Double score;
	public String label;

	public Predicate(String uri, Double score){
		this.uri = uri;
		this.score = score;
		this.label = getLabel(uri);
	}

	private String getLabel(String uri) {
		String[] splitted = uri.split("/");
		return splitted[splitted.length-1];
	}
}

public class GetDBPediaDifferencesBetweenPropertyAndOntologyPredicates {

	public static void main(String[] args) throws Exception {
		String questionnaire = "../evaluation/results/questionnaire-ALL.ods";
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		Sheet externals = document.getSheet("ALL");
		
		HashMap<String, List<Predicate>> goldStandard = new HashMap<String, List<Predicate>>();;
		for(int row=1; row<=1235; row++){
			try{
				String content = externals.getCellAt("A" + row).getTextValue();
				String groupId = "";
				while(!content.equals("DBPEDIA PROPERTY")){
					if(content.equals("GROUP ID")) 
						groupId = externals.getCellAt("A" + ++row).getTextValue();
					row++;
					content = externals.getCellAt("A" + row).getTextValue();
				}
				ArrayList<Predicate> groupPredicates = new ArrayList<Predicate>();
				while(!content.isEmpty()){
					if(content.startsWith("http://")){
						String propertyScore = externals.getCellAt("C" + row).getTextValue();
						if(!propertyScore.equals("")){
							groupPredicates.add(new Predicate(content, Double.parseDouble(propertyScore)));
						}
					}
					row++;
					content = externals.getCellAt("A" + row).getTextValue();
				}
				goldStandard.put(groupId, groupPredicates);
			}
			catch(Exception e){}
		}
		HashMap<String, List<Predicate>> couples = new HashMap<String, List<Predicate>>();
		for(String group : goldStandard.keySet()){
			List<Predicate> predicatesToKeep = new ArrayList<Predicate>();
			for(Predicate currentPredicate : goldStandard.get(group)){
				for(Predicate predicate : goldStandard.get(group)){
					if(currentPredicate.label.equals(predicate.label) && !currentPredicate.uri.equals(predicate.uri)){
						predicatesToKeep.add(currentPredicate);
						break;
					}
				}
			}
			couples.put(group, predicatesToKeep);
		}
		
		int similarPredicates = 0;
		int differentPredicates = 0;
		int total = 0;
		int totalProperty = 0;
		int totalOntology = 0;
		int totalFoaf = 0;
		int betterProperty = 0;
		int betterOntology = 0;
		int betterFoaf = 0;
		for(String group : couples.keySet()){
			for(Predicate currentPredicate : couples.get(group)){
				total++;
				for(Predicate predicate : couples.get(group)){
					if(currentPredicate.label.equals(predicate.label) && !currentPredicate.uri.equals(predicate.uri)){
						if(similarScore(currentPredicate.score, predicate.score))
							similarPredicates++;
						else{
							differentPredicates++;
							switch (checkContains(currentPredicate.uri)) {
							case 1:
								totalOntology++;
								if(currentPredicate.score > predicate.score) betterOntology++;
								break;
							case 2:
								totalProperty++;
								if(currentPredicate.score > predicate.score) betterProperty++;
								break;
							case 3:
								totalFoaf++;
								if(currentPredicate.score > predicate.score) betterFoaf++;
								break;
							}
						}
					}
				}
			}
		}
		System.out.println("Total predicates: " + total);
		System.out.println("Similar predicates: " + similarPredicates);
		System.out.println("Different predicates: " + differentPredicates);
		System.out.println("Ontology is better in " + betterOntology + " cases (" + (double)betterOntology/totalOntology*100 + "%)");
		System.out.println("Property is better in " + betterProperty + " cases (" + (double)betterProperty/totalProperty*100 + "%)");
		System.out.println("Foaf is better in " + betterFoaf + " cases ("+ (double)betterFoaf/totalFoaf*100 + "%)");
	}

	private static boolean similarScore(Double score1, Double score2) {
		double diff = score1 - score2;
		if(diff <= 0.30 && diff >= -0.30)
			return true;
		else
			return false;
	}
	
	private static int checkContains(String uri){
		if(uri.contains("ontology")) return 1;
		if(uri.contains("property")) return 2;
		if(uri.contains("foaf")) return 3;
		return 0;
	}
}
