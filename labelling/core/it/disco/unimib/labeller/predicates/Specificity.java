package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.ContextualizedValues;

public interface Specificity{
	
	public double of(ContextualizedValues request) throws Exception;
}