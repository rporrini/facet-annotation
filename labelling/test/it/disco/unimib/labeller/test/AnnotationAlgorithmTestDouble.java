package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;

class AnnotationAlgorithmTestDouble implements AnnotationAlgorithm{

	private ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();

	public AnnotationAlgorithmTestDouble thatReturns(String result){
		results.add(new AnnotationResult(result, 1));
		return this;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		return results;
	}
}