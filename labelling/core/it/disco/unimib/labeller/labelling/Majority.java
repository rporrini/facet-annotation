package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Majority implements AnnotationAlgorithm{

	private Index index;
	private double threshold;
	private SelectionCriterion query;

	public Majority(Index candidates, double threshold, SelectionCriterion query){
		this.index = candidates;
		this.threshold = threshold;
		this.query = query;
	}
	
	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<CandidatePredicate>> values = new CandidatePredicatesReport(new CandidatePredicates(index)).forValues(context, elements.toArray(new String[elements.size()]), query);
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