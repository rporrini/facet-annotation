package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.SelectionCriterion;

public interface Predicates {

	public Distribution forValues(ContextualizedValues request, SelectionCriterion query) throws Exception;

}