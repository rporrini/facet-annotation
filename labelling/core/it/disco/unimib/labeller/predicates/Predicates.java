package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.TripleSelectionCriterion;

public interface Predicates {

	public Distribution forValues(String context, String[] values, TripleSelectionCriterion query) throws Exception;

}