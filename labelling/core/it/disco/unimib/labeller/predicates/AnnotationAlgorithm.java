package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;

import java.util.List;

public interface AnnotationAlgorithm {

	public List<CandidateResource> typeOf(String context, List<String> elements) throws Exception;
}