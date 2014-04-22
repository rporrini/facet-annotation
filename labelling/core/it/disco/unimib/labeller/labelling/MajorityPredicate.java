package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityPredicate implements AnnotationAlgorithm{

	private CandidatePredicates candidates;
	private double threshold;

	public MajorityPredicate(CandidatePredicates candidates, double threshold){
		this.candidates = candidates;
		this.threshold = threshold;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]));
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		for(String value : values.keySet()){
			for(AnnotationResult result : values.get(value)){
				if(!predicateCounts.containsKey(result.value())) predicateCounts.put(result.value(), 0.0);
				predicateCounts.put(result.value(), predicateCounts.get(result.value()) + 1);
			}
		}
		List<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : predicateCounts.keySet()){
			double count = predicateCounts.get(predicate);
			double percentage = count / (double) elements.size();
			if(percentage > threshold){
				results.add(new AnnotationResult(predicate, percentage));
			}
		}
		Collections.sort(results);
		return results;
	}
}