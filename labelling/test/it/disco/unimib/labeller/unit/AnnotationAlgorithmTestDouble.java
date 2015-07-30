package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new CandidateResource(result));
		return this;
	}
	
	@Override
	public List<CandidateResource> annotate(ContextualizedValues parameterObject) throws Exception {
		return results;
	}
}