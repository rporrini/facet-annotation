package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PredicateMaximumLikelihood implements AnnotationAlgorithm{

	private Index index;
	private SelectionCriterion query;

	public PredicateMaximumLikelihood(Index candidates, SelectionCriterion query){
		this.index = candidates;
		this.query = query;
	}
	
	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<CandidatePredicate>> values = new CandidatePredicatesReport(new CandidatePredicates(index)).forValues(context, elements.toArray(new String[elements.size()]), query);

		Distribution distribution = new Distribution(values);
		
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(distribution);
		NormalizedPrior prior = new NormalizedPrior(distribution, unnormalizedPrior);
		
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(distribution, prior);
		NormalizedConditional conditional = new NormalizedConditional(distribution, prior, unnormalizedConditional);
		
		NormalizedMaximumLikelihood likelihood = new NormalizedMaximumLikelihood(distribution, conditional, prior);
		List<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : distribution.predicates()){
			results.add(new CandidatePredicate(predicate, likelihood.of(predicate)));
		}
		Collections.sort(results);
		
		return results;
	}
}