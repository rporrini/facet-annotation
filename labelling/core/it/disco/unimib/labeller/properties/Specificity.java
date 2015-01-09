package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ContextualizedValues;

public interface Specificity{
	
	public double of(ContextualizedValues request) throws Exception;
}