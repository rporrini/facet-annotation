package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Distribution{
	
	private HashMap<String, HashMap<String, Double>> scores;
	private Set<String> values;
	
	public Distribution(HashMap<String, List<AnnotationResult>> valueDistribution) {
		this.scores = invert(valueDistribution);
		this.values = enumerateValues();
	}
	
	public double scoreOf(String predicate, String value) {
		Double score = scores.get(predicate).get(value);
		if(score == null){
			score = 0.0;
		}
		return score;
	}
	
	public double totalScoreOf(String value){
		double result = 0;
		for(String predicate : predicates()){
			result+=scoreOf(predicate, value);
		}
		return result;
	}
	
	public Set<String> predicates(){
		return scores.keySet();
	}
	
	public Set<String> values(){
		return values;
	}
	
	private Set<String> enumerateValues(){
		HashSet<String> values = new HashSet<String>();
		for(String predicate : predicates()){
			values.addAll(scores.get(predicate).keySet());
		}
		return values;
	}
	
	private HashMap<String, HashMap<String, Double>> invert(HashMap<String, List<AnnotationResult>> valueDistribution) {
		HashMap<String, HashMap<String, Double>> inverted = new HashMap<String, HashMap<String, Double>>();
		for(String value : valueDistribution.keySet()){
			for(AnnotationResult predicate : valueDistribution.get(value)){
				if(!inverted.containsKey(predicate.value())) {
					inverted.put(predicate.value(), new HashMap<String, Double>());
				}
				HashMap<String, Double> predicates = inverted.get(predicate.value());
				if(!predicates.containsKey(value)){
					predicates.put(value, 0.0);
				}
				predicates.put(value, predicates.get(value) + predicate.score());
			}
		}
		return inverted;
	}
}