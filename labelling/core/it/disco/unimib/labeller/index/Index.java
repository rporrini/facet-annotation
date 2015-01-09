package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.predicates.AnnotationRequest;


public interface Index {

	public CandidateResourceSet get(AnnotationRequest request, TripleSelectionCriterion query) throws Exception;

	public long countPredicatesInContext(String predicate, String context, TripleSelectionCriterion query) throws Exception;
}