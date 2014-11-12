package it.disco.unimib.labeller.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class InterUserAgreement {
	public static void main(String[] args) throws Exception {
		String file = "../evaluation/gold-standards/questionnaires/questionnaire-ALL.ods";
		List<HashMap<Long, List<Double>>> ratings = getAllRatingsFrom(SpreadSheet.createFromFile(new File(file)));
		
		DescriptiveStatistics spearmanStatistic = new DescriptiveStatistics();
		DescriptiveStatistics pearsonStatistic = new DescriptiveStatistics();
		DescriptiveStatistics kappaStatistic = new DescriptiveStatistics();
		DescriptiveStatistics limitStatistic = new DescriptiveStatistics();
		
		for(int first = 0; first < ratings.size(); first++){
			for(int second = first + 1; second < ratings.size(); second++){
				List<Long> commonIds = new ArrayList<Long>(ratings.get(first).keySet());
				commonIds.retainAll(ratings.get(second).keySet());
				
				for(Long id : commonIds){
					List<Double> firstRatings = ratings.get(first).get(id);
					List<Double> secondRatings = ratings.get(second).get(id);
					
					Double spearman = new SpearmansCorrelation().correlation(convert(firstRatings), convert(secondRatings));
					if(spearman.equals(Double.NaN)) spearman = 1d;
					spearmanStatistic.addValue(spearman);
					
					Double pearson = new PearsonsCorrelation().correlation(convert(firstRatings), convert(secondRatings));
					if(pearson.equals(Double.NaN)) pearson = 1d;
					pearsonStatistic.addValue(pearson);
					
					Double kappa = new CohensKappa().kappa(merge(firstRatings, secondRatings));
					if(kappa.equals(Double.NaN)) kappa = 1d;
					kappaStatistic.addValue(kappa);
					
					for(int i=0; i < firstRatings.size(); i++){
						double delta = firstRatings.get(i) - secondRatings.get(i);
						limitStatistic.addValue(delta);
					}
				}
			}
		}
		
		System.out.println();
		System.out.println("Mean Spearman Correlation: " + spearmanStatistic.getMean() + " (+/- " + spearmanStatistic.getVariance() + ")");
		System.out.println("Mean Pearsons Correlation: " + pearsonStatistic.getMean() + " (+/- " + pearsonStatistic.getVariance() + ")");
		System.out.println("Mean Cohen's Kappa: " + kappaStatistic.getMean() + " (+/- " + kappaStatistic.getVariance() + ")");
		System.out.println("Mean Disagreement: " + limitStatistic.getMean()  + " (+/- " + limitStatistic.getVariance() + ")");
	}
	
	private static double[] convert(List<Double> scores){
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
