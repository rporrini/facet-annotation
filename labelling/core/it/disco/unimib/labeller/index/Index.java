package it.disco.unimib.labeller.index;


public interface Index {

	public CandidateResourceSet get(String value, String context, TripleSelectionCriterion query) throws Exception;

	public long countPredicatesInContext(String predicate, String context, TripleSelectionCriterion query) throws Exception;
}