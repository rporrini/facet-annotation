package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.GroupBySearch;

public class ValueForPredicate implements Discriminacy{

	private GroupBySearch index;

	public ValueForPredicate(GroupBySearch index) {
		this.index = index;
	}

	@Override
	public double of(String predicate, String value, double frequencyOfPredicate) throws Exception {
		double frequencyOfValueAndPredicate = index.countValuesForPredicates(value, predicate);
		if(frequencyOfPredicate > 0 && frequencyOfValueAndPredicate > 0){
			return Math.log((frequencyOfValueAndPredicate/frequencyOfPredicate) + 1);
		}
		return 0;
	}
}