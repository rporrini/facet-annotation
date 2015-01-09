package it.disco.unimib.labeller.index;


public interface TripleSelectionCriterion{
	
	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception;
}