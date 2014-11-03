package it.disco.unimib.labeller.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class InterUserAgreement {
	public static void main(String[] args) throws Exception {
		String file = "../evaluation/gold-standards/questionnaires/questionnaire-ALL.ods";
		List<HashMap<Long, List<Double>>> ratings = getAllRatingsFrom(SpreadSheet.createFromFile(new File(file)));
		
		for(Long id : ratings.get(0).keySet()){
			for(int first=0; first < ratings.size(); first++){
				List<Double> firstRates = ratings.get(first).get(id);
				if(firstRates == null) continue;
				for(int second = first + 1; second < ratings.size() ; second++){
					List<Double> secondRates = ratings.get(second).get(id);
					if(secondRates == null) continue;
					
					double[] f = convert(firstRates);
					double[] s = convert(secondRates);
					
					System.out.print(id + " between " + first + " and " + second + ": ");
					double correlation = new PearsonsCorrelation().correlation(f, s);
					System.out.println(correlation);
				}
			}
		}
	}
	
	private static double[] convert(List<Double> scores){
		double[] result = new double[scores.size()];
		for(int i=0; i<scores.size();i++) result[i] = scores.get(i);
		return result;
	}
	
	private static List<HashMap<Long, List<Double>>> getAllRatingsFrom(SpreadSheet questionnaire) {
		List<HashMap<Long, List<Double>>> scores = new ArrayList<HashMap<Long, List<Double>>>();
		
		for(int i=1; i < questionnaire.getSheetCount()-2; i++){
			final HashMap<Long, List<Double>> rates = new HashMap<Long, List<Double>>();
			
			QuestionnaireRatings ratings = new QuestionnaireRatings(new Rating() {
				@Override
				public void store(Long id, String content, String propertyScore, List<String> rows) {
					if(propertyScore.isEmpty()){
						return;
					}
					if(propertyScore.equals("#DIV/0!")){
						propertyScore = "0";
					}
					if(!rates.containsKey(id)) rates.put(id, new ArrayList<Double>());
					
					rates.get(id).add(Double.parseDouble(propertyScore));
				}
			});
			ratings.getAnswers(questionnaire.getSheet(i));
			scores.add(rates);
		}
		return scores;
	}
}
