package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.FullTextQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Majority implements AnnotationAlgorithm{

	private Predicates candidates;
	private double threshold;
	private FullTextQuery query;

	public Majority(Predicates candidates, double threshold, FullTextQuery query){
		this.candidates = candidates;
		this.threshold = threshold;
		this.query = query;
	}
	
	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<CandidatePredicate>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]), query);
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		for(String value : values.keySet()){
			for(CandidatePredicate result : values.get(value)){
				if(!predicateCounts.containsKey(result.value())) predicateCounts.put(result.value(), 0.0);
				predicateCounts.put(result.value(), predicateCounts.get(result.value()) + 1);
			}
		}
		List<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : predicateCounts.keySet()){
			double count = predicateCounts.get(predicate);
			double percentage = count / (double) elements.size();
			if(percentage > threshold){
				results.add(new CandidatePredicate(predicate, percentage));
			}
		}
		Collections.sort(results);
		return results;
	}
}