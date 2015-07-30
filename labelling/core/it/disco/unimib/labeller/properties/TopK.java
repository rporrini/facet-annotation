package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;

import java.util.List;

public class TopK implements AnnotationAlgorithm{

	private AnnotationAlgorithm algorithm;
	private int topK;

	public TopK(int k, AnnotationAlgorithm algorithm){
		this.algorithm = algorithm;
		this.topK = k;
	}
	
	@Override
	public List<CandidateResource> annotate(ContextualizedValues parameterObject) throws Exception {
		List<CandidateResource> results = algorithm.annotate(parameterObject);
		return results.subList(0, Math.min(topK, results.size()));
	}
}