package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.index.CandidatePredicate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class AnalizeQuestionnaireAnswers {

	public static void main(String[] args) throws Exception {
		String questionnaire = "../evaluation/gold-standards/questionnaires/questionnaire-ALL.ods";
		SpreadSheet document = SpreadSheet.createFromFile(new File(questionnaire));
		ArrayList<String> sheets = new ArrayList<String>();
		for(int i = 0; i < document.getSheetCount(); i++){
			sheets.add(document.getSheet(i).getName());
		}
		sheets.remove("first");
		sheets.remove("last");
		int globalSimilarPredicates = 0;
		int globalDifferentPredicates = 0;
		int globalTotalPredicates = 0;
		int globalTotalProperty = 0;
		int globalTotalOntology = 0;
		int globalTotalFoaf = 0;
		int globalBetterProperty = 0;
		int globalBetterOntology = 0;
		int globalBetterFoaf = 0;
		
		for(String sheetName : sheets){
			Sheet externals = document.getSheet(sheetName);
			
			HashMap<String, List<CandidatePredicate>> goldStandard = new HashMap<String, List<CandidatePredicate>>();;
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
					ArrayList<CandidatePredicate> groupPredicates = new ArrayList<CandidatePredicate>();
					while(!content.isEmpty()){
						if(content.startsWith("http://")){
							String propertyScore = externals.getCellAt("C" + row).getTextValue();
							if(!propertyScore.equals("")){
								groupPredicates.add(new CandidatePredicate(content, Double.parseDouble(propertyScore)));
							}
						}
						row++;
						content = externals.getCellAt("A" + row).getTextValue();
					}
					goldStandard.put(groupId, groupPredicates);
				}
				catch(Exception e){}
			}
			HashMap<String, List<CandidatePredicate>> couples = new HashMap<String, List<CandidatePredicate>>();
			for(String group : goldStandard.keySet()){
				List<CandidatePredicate> predicatesToKeep = new ArrayList<CandidatePredicate>();
				for(CandidatePredicate currentPredicate : goldStandard.get(group)){
					for(CandidatePredicate predicate : goldStandard.get(group)){
						if(currentPredicate.label().equals(predicate.label()) && !currentPredicate.value().equals(predicate.value())){
							predicatesToKeep.add(currentPredicate);
							break;
						}
					}
				}
				couples.put(group, predicatesToKeep);
			}
			
			int similarPredicates = 0;
			int differentPredicates = 0;
			int totalPredicates = 0;
			int totalProperty = 0;
			int totalOntology = 0;
			int totalFoaf = 0;
			int betterProperty = 0;
			int betterOntology = 0;
			int betterFoaf = 0;
			for(String group : couples.keySet()){
				for(CandidatePredicate currentPredicate : couples.get(group)){
					totalPredicates++;
					for(CandidatePredicate predicate : couples.get(group)){
						if(currentPredicate.label().equals(predicate.label()) && !currentPredicate.value().equals(predicate.value())){
							if(similarScore(currentPredicate.score(), predicate.score()))
								similarPredicates++;
							else{
								differentPredicates++;
								switch (checkContains(currentPredicate.value())) {
								case 1:
									totalOntology++;
									if(currentPredicate.score() > predicate.score()) betterOntology++;
									break;
								case 2:
									totalProperty++;
									if(currentPredicate.score() > predicate.score()) betterProperty++;
									break;
								case 3:
									totalFoaf++;
									if(currentPredicate.score() > predicate.score()) betterFoaf++;
									break;
								}
							}
						}
					}
				}
			}
			if(!sheetName.equals("ALL")){
				globalSimilarPredicates += similarPredicates;
				globalDifferentPredicates += differentPredicates;
				globalTotalPredicates += totalPredicates;
				globalTotalProperty += totalProperty;
				globalTotalOntology += totalOntology;
				globalTotalFoaf += totalFoaf;
				globalBetterProperty += betterProperty;
				globalBetterOntology += betterOntology;
				globalBetterFoaf += betterFoaf;
			}
			
			System.out.println("Sheet: " + sheetName);
			System.out.println("Total predicates: " + totalPredicates);
			System.out.println("Similar predicates: " + similarPredicates);
			System.out.println("Different predicates: " + differentPredicates);
			System.out.println("Ontology is better in " + betterOntology + " cases (" + (double)betterOntology/totalOntology*100 + "%)");
			System.out.println("Property is better in " + betterProperty + " cases (" + (double)betterProperty/totalProperty*100 + "%)");
			System.out.println("Foaf is better in " + betterFoaf + " cases ("+ (double)betterFoaf/totalFoaf*100 + "%)");
			System.out.println();
		}
		System.out.println("Global results");
		System.out.println("Total predicates: " + globalTotalPredicates);
		System.out.println("Similar predicates: " + globalSimilarPredicates);
		System.out.println("Different predicates: " + globalDifferentPredicates);
		System.out.println("Ontology is better in " + globalBetterOntology + " cases (" + (double)globalBetterOntology/globalTotalOntology*100 + "%)");
		System.out.println("Property is better in " + globalBetterProperty + " cases (" + (double)globalBetterProperty/globalTotalProperty*100 + "%)");
		System.out.println("Foaf is better in " + globalBetterFoaf + " cases ("+ (double)globalBetterFoaf/globalTotalFoaf*100 + "%)");
		System.out.println();
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
