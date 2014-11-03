package it.disco.unimib.labeller.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class InterUserAgreement {
	public static void main(String[] args) throws Exception {
		String file = "../evaluation/gold-standards/questionnaires/questionnaire-ALL.ods";
		List<HashMap<Long, List<Double>>> ratings = getAllRatingsFrom(SpreadSheet.createFromFile(new File(file)));
		
		DescriptiveStatistics correlationStatistic = new DescriptiveStatistics();
		DescriptiveStatistics kappaStatistic = new DescriptiveStatistics();
		DescriptiveStatistics limitStatistic = new DescriptiveStatistics();
		
		for(int first = 0; first < ratings.size(); first++){
			for(int second = first + 1; second < ratings.size(); second++){
				List<Long> commonIds = new ArrayList<Long>(ratings.get(first).keySet());
				commonIds.retainAll(ratings.get(second).keySet());
				
				if(commonIds.isEmpty()) continue;
				
				List<Double> firstRatings = new ArrayList<Double>();
				for(Long id : commonIds){
					firstRatings.addAll(ratings.get(first).get(id));
				}
				
				List<Double> secondRatings = new ArrayList<Double>();
				for(Long id : commonIds){
					secondRatings.addAll(ratings.get(second).get(id));
				}
				
				double correlation = new SpearmansCorrelation().correlation(convertForSpearman(firstRatings), convertForSpearman(secondRatings));
				correlationStatistic.addValue(correlation);
				System.out.println("Spearman Correlation between " + first + " and " + second + ": " + correlation);
				
				double kappa = new CohensKappa().kappa(merge(firstRatings, secondRatings));
				kappaStatistic.addValue(kappa);
				System.out.println("Cohen's Kappa between " + first + " and " + second + ": " + kappa);
				
				for(int i=0; i < firstRatings.size(); i++){
					double delta = firstRatings.get(i) - secondRatings.get(i);
					limitStatistic.addValue(delta);
				}
			}
		}
		System.out.println();
		System.out.println("Mean Spearman Correlation: " + correlationStatistic.getMean());
		System.out.println("Mean Cohen's Kappa: " + kappaStatistic.getMean());
		System.out.println("Mean Disagreement: " + limitStatistic.getMean());
		System.out.println("Mean Disagreement Variance: " + limitStatistic.getVariance());
	}
	
	private static double[] convertForSpearman(List<Double> scores){
		double[] result = new double[scores.size()];
		for(int i=0; i<scores.size();i++) result[i] = scores.get(i);
		return result;
	}
	
	private static List<List<Integer>> merge(List<Double> first, List<Double> second){
		ArrayList<List<Integer>> result = new ArrayList<List<Integer>>();
		result.add(convertForCohens(first));
		result.add(convertForCohens(second));
		return result;
	}
	
	private static List<Integer> convertForCohens(List<Double> scores){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Double score : scores){
			result.add(score.intValue());
		}
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
					
					rates.get(id).add((double)Math.round(Double.parseDouble(propertyScore)));
				}
			});
			ratings.getAnswers(questionnaire.getSheet(i));
			scores.add(rates);
		}
		return scores;
	}
}
