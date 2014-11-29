package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PredicateMaximumLikelihood implements AnnotationAlgorithm{

	private Index index;
	private TripleSelectionCriterion query;

	public PredicateMaximumLikelihood(Index candidates, TripleSelectionCriterion query){
		this.index = candidates;
		this.query = query;
	}
	
	@Override
	public List<CandidateResource> typeOf(String context, List<String> elements) throws Exception {
		Distribution distribution = new CandidatePredicates(index).forValues(context, elements.toArray(new String[elements.size()]), query);
		
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(distribution);
		NormalizedPrior prior = new NormalizedPrior(distribution, unnormalizedPrior);
		
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(distribution, prior);
		NormalizedConditional conditional = new NormalizedConditional(distribution, prior, unnormalizedConditional);
		
		NormalizedMaximumLikelihood likelihood = new NormalizedMaximumLikelihood(distribution, conditional, prior);
		List<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : distribution.predicates()){
			CandidateResource e = new CandidateResource(predicate);
			e.sumScore(likelihood.of(predicate));
			results.add(e);
		}
		Collections.sort(results);
		
		return results;
	}
}