package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;

public class PredicateContextSpecificity implements Specificity{

	private Index index;

	public PredicateContextSpecificity(Index index) {
		this.index = index;
	}
	
	@Override
	public double of(String predicate, String domain) throws Exception {
		double frequencyOfPredicateInDomain = index.countPredicatesInContext(predicate, domain, new PartialContext(new AllValues()));
		double frequencyOfPredicate = index.countPredicatesInContext(predicate, domain, new NoContext(new AllValues()));;
		return frequencyOfPredicateInDomain / frequencyOfPredicate;
	}
}