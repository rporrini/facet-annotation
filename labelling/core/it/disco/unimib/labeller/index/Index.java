package it.disco.unimib.labeller.index;



public interface Index {

	public CandidateResourceSet get(ContextualizedValues request, TripleSelectionCriterion query) throws Exception;

	public long countPredicatesInContext(ContextualizedValues request, TripleSelectionCriterion query) throws Exception;
}