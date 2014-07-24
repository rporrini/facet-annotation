package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;


public class PredicateWeight{
	
	private GroupBySearch index;
	private boolean predicateWeighting;
	private long frequencyOfPredicate;
	private boolean valueWeighting;

	public PredicateWeight() {
		this.predicateWeighting = false;
		this.valueWeighting = false;
	}

	public PredicateWeight(GroupBySearch index, boolean valueWeight) {
		this.index = index;
		this.predicateWeighting = true;
		this.valueWeighting = valueWeight;
	}
	
	public double predicateInContextDiscriminacy(String predicate, String context) throws Exception{
		if(predicateWeighting){
			this.frequencyOfPredicate = index.count(predicate, context, new OptionalContext());
			double frequencyOfPredicateAndContext = index.count(predicate, context, new MandatoryContext());
			return Math.log((frequencyOfPredicateAndContext/frequencyOfPredicate) + 1.1);
		}
		return 1;
	}

	public double predicateAndValueDiscriminacy(String predicate, String value, Distribution distribution) {
		if(valueWeighting){
			double frequencyOfValueAndPredicate = distribution.scoreOf(predicate, value);
			return Math.log(frequencyOfValueAndPredicate/frequencyOfPredicate);
		}
		return 1;
	}
}