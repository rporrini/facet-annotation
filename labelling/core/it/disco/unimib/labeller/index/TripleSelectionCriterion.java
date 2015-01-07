package it.disco.unimib.labeller.index;


public interface TripleSelectionCriterion{
	
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception;
}