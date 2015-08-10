package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new CandidateProperty(result));
		return this;
	}
	
	@Override
	public List<CandidateProperty> annotate(ContextualizedValues parameterObject) throws Exception {
		return results;
	}
}