package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new CandidateResource(result, 1));
		return this;
	}
	
	@Override
	public List<CandidateResource> typeOf(String context, List<String> elements) throws Exception {
		return results;
	}
}