package it.disco.unimib.labeller.labelling;

public class ValueForPredicate implements Discriminacy{

	@Override
	public double of(String predicate, String value, double frequencyOfPredicate, Distribution distribution) throws Exception {
		double frequencyOfValueAndPredicate = distribution.scoreOf(predicate, value);
		if(frequencyOfPredicate > 0 && frequencyOfValueAndPredicate > 0){
			return Math.log((frequencyOfValueAndPredicate/frequencyOfPredicate) + 1);
		}
		return 0;
	}
}