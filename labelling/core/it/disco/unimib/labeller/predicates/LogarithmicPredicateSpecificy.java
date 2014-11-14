package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

public class LogarithmicPredicateSpecificy implements Specificity{
	
	private Index index;
	private SelectionCriterion selectionCriterion;
	
	public LogarithmicPredicateSpecificy(Index index, SelectionCriterion selectionCriterion) {
		this.index = index;
		this.selectionCriterion = selectionCriterion;
	}
	
	@Override
	public double of(String predicate, String context, double frequencyOfPredicate) throws Exception{
		double frequencyOfPredicateAndContext = index.countPredicatesInContext(predicate, context, selectionCriterion);
		return Math.log((frequencyOfPredicateAndContext/frequencyOfPredicate) + 1.1);
	}
}