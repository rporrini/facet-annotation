package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.index.CandidateProperty;

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
		int globalSimilarProperties = 0;
		int globalDifferentProperties = 0;
		int globalTotalProperties = 0;
		int globalTotalProperty = 0;
		int globalTotalOntology = 0;
		int globalTotalFoaf = 0;
		int globalBetterProperty = 0;
		int globalBetterOntology = 0;
		int globalBetterFoaf = 0;
		
		for(String sheetName : sheets){
			Sheet externals = document.getSheet(sheetName);
			
			HashMap<String, List<CandidateProperty>> goldStandard = new HashMap<String, List<CandidateProperty>>();;
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
					ArrayList<CandidateProperty> groupProperties = new ArrayList<CandidateProperty>();
					while(!content.isEmpty()){
						if(content.startsWith("http://")){
							String propertyScore = externals.getCellAt("C" + row).getTextValue();
							if(!propertyScore.equals("")){
								CandidateProperty e = new CandidateProperty(content);
								e.sumScore(Double.parseDouble(propertyScore));
								groupProperties.add(e);
							}
						}
						row++;
						content = externals.getCellAt("A" + row).getTextValue();
					}
					goldStandard.put(groupId, groupProperties);
				}
				catch(Exception e){}
			}
			HashMap<String, List<CandidateProperty>> couples = new HashMap<String, List<CandidateProperty>>();
			for(String group : goldStandard.keySet()){
				List<CandidateProperty> propertiesToKeep = new ArrayList<CandidateProperty>();
				for(CandidateProperty currentProperty : goldStandard.get(group)){
					for(CandidateProperty property : goldStandard.get(group)){
						if(currentProperty.label().equals(property.label()) && !currentProperty.uri().equals(property.uri())){
							propertiesToKeep.add(currentProperty);
							break;
						}
					}
				}
				couples.put(group, propertiesToKeep);
			}
			
			int similarProperties = 0;
			int differentProperties = 0;
			int totalProperties = 0;
			int totalProperty = 0;
			int totalOntology = 0;
			int totalFoaf = 0;
			int betterProperty = 0;
			int betterOntology = 0;
			int betterFoaf = 0;
			for(String group : couples.keySet()){
				for(CandidateProperty currentProperty : couples.get(group)){
					totalProperties++;
					for(CandidateProperty property : couples.get(group)){
						if(currentProperty.label().equals(property.label()) && !currentProperty.uri().equals(property.uri())){
							if(similarScore(currentProperty.score(), property.score()))
								similarProperties++;
							else{
								differentProperties++;
								switch (checkContains(currentProperty.uri())) {
								case 1:
									totalOntology++;
									if(currentProperty.score() > property.score()) betterOntology++;
									break;
								case 2:
									totalProperty++;
									if(currentProperty.score() > property.score()) betterProperty++;
									break;
								case 3:
									totalFoaf++;
									if(currentProperty.score() > property.score()) betterFoaf++;
									break;
								}
							}
						}
					}
				}
			}
			if(!sheetName.equals("ALL")){
				globalSimilarProperties += similarProperties;
				globalDifferentProperties += differentProperties;
				globalTotalProperties += totalProperties;
				globalTotalProperty += totalProperty;
				globalTotalOntology += totalOntology;
				globalTotalFoaf += totalFoaf;
				globalBetterProperty += betterProperty;
				globalBetterOntology += betterOntology;
				globalBetterFoaf += betterFoaf;
			}
			
			System.out.println("Sheet: " + sheetName);
			System.out.println("Total properties: " + totalProperties);
			System.out.println("Similar properties: " + similarProperties);
			System.out.println("Different properties: " + differentProperties);
			System.out.println("Ontology is better in " + betterOntology + " cases (" + (double)betterOntology/totalOntology*100 + "%)");
			System.out.println("Property is better in " + betterProperty + " cases (" + (double)betterProperty/totalProperty*100 + "%)");
			System.out.println("Foaf is better in " + betterFoaf + " cases ("+ (double)betterFoaf/totalFoaf*100 + "%)");
			System.out.println();
		}
		System.out.println("Global results");
		System.out.println("Total properties: " + globalTotalProperties);
		System.out.println("Similar properties: " + globalSimilarProperties);
		System.out.println("Different properties: " + globalDifferentProperties);
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
