package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

public class SimpleContextForPredicate implements Specificity{

	private Index index;
	private SelectionCriterion selectionCriterion;

	public SimpleContextForPredicate(Index index, SelectionCriterion selectionCriterion) {
		this.index = index;
		this.selectionCriterion = selectionCriterion;
	}
	
	@Override
	public double of(String predicate, String contextOrValue, double frequencyOfPredicate) throws Exception {
		double frequencyOfPredicateAndContext = index.countPredicatesInContext(predicate, contextOrValue, selectionCriterion);
		return frequencyOfPredicateAndContext/frequencyOfPredicate;
	}
}