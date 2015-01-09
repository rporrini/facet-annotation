package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

public interface Predicates {

	public Distribution forValues(ContextualizedValues request, TripleSelectionCriterion query) throws Exception;

}