package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.AnnotationRequest;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new CandidateResource(result));
		return this;
	}
	
	@Override
	public List<CandidateResource> typeOf(AnnotationRequest parameterObject) throws Exception {
		return results;
	}
}