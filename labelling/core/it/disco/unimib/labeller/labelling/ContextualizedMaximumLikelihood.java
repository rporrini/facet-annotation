package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.Index;

import java.util.ArrayList;
import java.util.List;

public class ContextualizedMaximumLikelihood implements AnnotationAlgorithm{

	private Index index;

	public ContextualizedMaximumLikelihood(Index index) {
		this.index = index;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		new CandidatePredicates(index);
		
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		return results;
	}
}
