package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.MandatoryContext;

public class PredicateAndContextWeight implements PredicateWeight{
	
	private Index index;
	
	public PredicateAndContextWeight(Index index) {
		this.index = index;
	}
	
	@Override
	public double discriminacy(String predicate, String context, double frequencyOfPredicate, Distribution distribution) throws Exception{
		double frequencyOfPredicateAndContext = index.count(predicate, context, new MandatoryContext());
		return Math.log((frequencyOfPredicateAndContext/frequencyOfPredicate) + 1.1);
	}
}