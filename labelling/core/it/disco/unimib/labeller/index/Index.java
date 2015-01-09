package it.disco.unimib.labeller.index;



public interface Index {

	public CandidateResourceSet get(ContextualizedValues request, SelectionCriterion query) throws Exception;

	public long count(ContextualizedValues request, SelectionCriterion query) throws Exception;
}