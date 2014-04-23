package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MaximumLikelihoodPredicate implements AnnotationAlgorithm{

	private CandidatePredicates candidates;

	public MaximumLikelihoodPredicate(CandidatePredicates candidates){
		this.candidates = candidates;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]));
		Distribution distribution = new Distribution(values);
		MaximumLikelihood likelihood = new MaximumLikelihood(distribution);
		ArrayList<Double> scores = new ArrayList<Double>();
		for(String predicate : distribution.predicates()){
			scores.add(likelihood.of(predicate));
		}
		Normalize normalize = new Normalize(scores.toArray(new Double[scores.size()]));
		List<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : distribution.predicates()){
			results.add(new AnnotationResult(predicate, normalize.value(likelihood.of(predicate))));
		}
		Collections.sort(results);
		return results;
	}
}