package it.disco.unimib.labeller.index;

public interface SelectionCriterion{
	
	public Constraint asQuery(ContextualizedValues values) throws Exception;
}