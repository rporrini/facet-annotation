package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AnnotationWithPredicate implements AnnotationAlgorithm{

	private CandidatePredicates candidates;

	public AnnotationWithPredicate(CandidatePredicates candidates){
		this.candidates = candidates;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]));
		Distribution distribution = new Distribution(values);
		MaximumLikelihood likelihood = new MaximumLikelihood(distribution);
		List<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : distribution.predicates()){
			results.add(new AnnotationResult(predicate, likelihood.of(predicate)));
		}
		Collections.sort(results);
		return results;
	}
}