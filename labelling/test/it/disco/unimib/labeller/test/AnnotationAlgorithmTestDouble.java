package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new CandidatePredicate(result, 1));
		return this;
	}
	
	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		return results;
	}
}