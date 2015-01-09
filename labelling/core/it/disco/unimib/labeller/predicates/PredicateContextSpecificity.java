package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.ContextualizedPredicate;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyPredicate;

public class PredicateContextSpecificity implements Specificity{

	private Index index;
	private IndexFields indexFields;

	public PredicateContextSpecificity(Index index, IndexFields fields) {
		this.index = index;
		this.indexFields = fields;
	}
	
	@Override
	public double of(ContextualizedValues request) throws Exception {
		double frequencyOfPredicateInDomain = index.countPredicatesInContext(request, new ContextualizedPredicate(indexFields));
		double frequencyOfPredicate = index.countPredicatesInContext(request, new OnlyPredicate(indexFields));
		return frequencyOfPredicateInDomain / frequencyOfPredicate;
	}
}