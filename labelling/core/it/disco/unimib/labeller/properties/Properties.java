package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.SelectionCriterion;

public interface Properties {

	public PropertyDistribution forValues(ContextualizedValues request, SelectionCriterion query) throws Exception;

}