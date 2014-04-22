package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Distribution{
	private HashMap<String, HashMap<String, List<AnnotationResult>>> predicatesDistribution;
	
	public Distribution(HashMap<String, List<AnnotationResult>> valueDistribution) {
		this.predicatesDistribution = invert(valueDistribution);
	}
	
	public Set<String> predicates(){
		return predicatesDistribution.keySet();
	}
	
	public Set<String> values(){
		HashSet<String> values = new HashSet<String>();
		for(String predicate : predicates()){
			values.addAll(predicatesDistribution.get(predicate).keySet());
		}
		return values;
	}
	
	public double scoreOf(String predicate, String value) {
		List<AnnotationResult> list = this.predicatesDistribution.get(predicate).get(value);
		double count = 0.0;
		if(list != null){
			for(AnnotationResult result : list){
				count += result.score();
			}
		}
		return count;
	}
	
	private HashMap<String, HashMap<String, List<AnnotationResult>>> invert(HashMap<String, List<AnnotationResult>> valueDistribution) {
		HashMap<String, HashMap<String, List<AnnotationResult>>> inverted = new HashMap<String, HashMap<String, List<AnnotationResult>>>();
		for(String value : valueDistribution.keySet()){
			for(AnnotationResult predicate : valueDistribution.get(value)){
				if(!inverted.containsKey(predicate.value())) {
					inverted.put(predicate.value(), new HashMap<String, List<AnnotationResult>>());
				}
				if(!inverted.get(predicate.value()).containsKey(value)){
					inverted.get(predicate.value()).put(value, new ArrayList<AnnotationResult>());
				}
				inverted.get(predicate.value()).get(value).add(predicate);
			}
		}
		return inverted;
	}
}