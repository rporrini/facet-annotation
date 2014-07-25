package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.MandatoryContext;

public class ContextForPredicate implements Discriminacy{
	
	private Index index;
	
	public ContextForPredicate(Index index) {
		this.index = index;
	}
	
	@Override
	public double of(String predicate, String context, double frequencyOfPredicate, Distribution distribution) throws Exception{
		double frequencyOfPredicateAndContext = index.count(predicate, context, new MandatoryContext());
		return Math.log((frequencyOfPredicateAndContext/frequencyOfPredicate) + 1.1);
	}
}