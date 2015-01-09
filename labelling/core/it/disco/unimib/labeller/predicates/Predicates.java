package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.TripleSelectionCriterion;

public interface Predicates {

	public Distribution forValues(AnnotationRequest request, TripleSelectionCriterion query) throws Exception;

}