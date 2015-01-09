package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;

public class PredicateContextSpecificity implements Specificity{

	private Index index;
	private IndexFields indexFields;

	public PredicateContextSpecificity(Index index, IndexFields fields) {
		this.index = index;
		this.indexFields = fields;
	}
	
	@Override
	public double of(ContextualizedValues request) throws Exception {
		double frequencyOfPredicateInDomain = index.countPredicatesInContext(request, new PartialContext(new AllValues(indexFields)));
		double frequencyOfPredicate = index.countPredicatesInContext(request, new NoContext(new AllValues(indexFields)));
		return frequencyOfPredicateInDomain / frequencyOfPredicate;
	}
}