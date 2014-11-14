package it.disco.unimib.labeller.predicates;

public class Constant implements Specificity{

	@Override
	public double of(String predicate, String context, double frequencyOfPredicate) throws Exception {
		return 1;
	}
}