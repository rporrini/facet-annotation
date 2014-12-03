package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;

public class LogarithmicPredicateSpecificy implements Specificity{
	
	private Index index;
	
	public LogarithmicPredicateSpecificy(Index index) {
		this.index = index;
	}
	
	@Override
	public double of(String predicate, String context) throws Exception{
		double frequencyOfPredicateAndContext = index.countPredicatesInContext(predicate, context, new PartialContext(new AllValues()));
		double frequencyOfPredicate = index.countPredicatesInContext(predicate, context, new NoContext(new AllValues()));;
		return Math.log((frequencyOfPredicateAndContext/frequencyOfPredicate) + 1.1);
	}
}