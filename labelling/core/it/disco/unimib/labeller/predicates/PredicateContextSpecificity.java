package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.AllValues;
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
	public double of(String predicate, String domain) throws Exception {
		double frequencyOfPredicateInDomain = index.countPredicatesInContext(predicate, domain, new PartialContext(new AllValues(indexFields.analyzer())));
		double frequencyOfPredicate = index.countPredicatesInContext(predicate, domain, new NoContext(new AllValues(indexFields.analyzer())));
		return frequencyOfPredicateInDomain / frequencyOfPredicate;
	}
}