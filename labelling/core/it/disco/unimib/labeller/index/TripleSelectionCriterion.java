package it.disco.unimib.labeller.index;

public interface TripleSelectionCriterion{
	
	public Constraint asQuery(ContextualizedValues values) throws Exception;
}