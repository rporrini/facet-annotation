package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;

import java.util.List;

public class TopK implements AnnotationAlgorithm{

	private AnnotationAlgorithm algorithm;
	private int topK;

	public TopK(int k, AnnotationAlgorithm algorithm){
		this.algorithm = algorithm;
		this.topK = k;
	}
	
	@Override
	public List<CandidateResource> typeOf(String context, List<String> elements) throws Exception {
		List<CandidateResource> results = algorithm.typeOf(context, elements);
		return results.subList(0, Math.min(topK, results.size()));
	}
}