package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.SelectionCriterion;

public interface Properties {

	public Distribution forValues(ContextualizedValues request, SelectionCriterion query) throws Exception;

}