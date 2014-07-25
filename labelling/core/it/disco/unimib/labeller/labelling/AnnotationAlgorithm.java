package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;

import java.util.List;

public interface AnnotationAlgorithm {

	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception;
}