package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;

import java.util.List;

public class TopK implements AnnotationAlgorithm{

	private AnnotationAlgorithm algorithm;
	private int topK;

	public TopK(int k, AnnotationAlgorithm algorithm){
		this.algorithm = algorithm;
		this.topK = k;
	}
	
	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		List<CandidatePredicate> results = algorithm.typeOf(context, elements);
		return results.subList(0, Math.min(topK, results.size()));
	}
}