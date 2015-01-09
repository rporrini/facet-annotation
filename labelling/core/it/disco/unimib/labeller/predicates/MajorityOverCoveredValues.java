package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityOverCoveredValues implements AnnotationAlgorithm{

	private Index index;
	private double threshold;
	private TripleSelectionCriterion query;

	public MajorityOverCoveredValues(Index candidates, double threshold, TripleSelectionCriterion query){
		this.index = candidates;
		this.threshold = threshold;
		this.query = query;
	}
	
	@Override
	public List<CandidateResource> typeOf(ContextualizedValues request) throws Exception {
		Distribution values = new CandidatePredicates(index).forValues(request, query);
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		for(String value : values.values()){
			for(String result : values.predicates()){
				if(!predicateCounts.containsKey(result)) predicateCounts.put(result, 0.0);
				if(values.scoreOf(result, value) > 0) predicateCounts.put(result, predicateCounts.get(result) + 1);
			}
		}
		List<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : predicateCounts.keySet()){
			double count = predicateCounts.get(predicate);
			double percentage = count / (double) request.all().length;
			if(percentage > threshold){
				CandidateResource e = new CandidateResource(predicate);
				e.sumScore(percentage);
				results.add(e);
			}
		}
		Collections.sort(results);
		return results;
	}
}