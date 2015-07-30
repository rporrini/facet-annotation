package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;

import java.util.List;

public interface AnnotationAlgorithm {

	public List<CandidateResource> annotate(ContextualizedValues facet) throws Exception;
}
