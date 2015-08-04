package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;

import java.util.List;

public interface AnnotationAlgorithm {

	public List<CandidateProperty> annotate(ContextualizedValues facet) throws Exception;
}
