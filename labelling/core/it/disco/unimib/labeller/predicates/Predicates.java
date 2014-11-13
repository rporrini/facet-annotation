package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.SelectionCriterion;

public interface Predicates {

	public Distribution forValues(String context, String[] values, SelectionCriterion query) throws Exception;

}