package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

public class SimplePredicateSpeficity implements Specificity{

	private Index index;
	private TripleSelectionCriterion selectionCriterion;

	public SimplePredicateSpeficity(Index index, TripleSelectionCriterion selectionCriterion) {
		this.index = index;
		this.selectionCriterion = selectionCriterion;
	}
	
	@Override
	public double of(String predicate, String contextOrValue, double frequencyOfPredicate) throws Exception {
		double frequencyOfPredicateAndContext = index.countPredicatesInContext(predicate, contextOrValue, selectionCriterion);
		return frequencyOfPredicateAndContext/frequencyOfPredicate;
	}
}