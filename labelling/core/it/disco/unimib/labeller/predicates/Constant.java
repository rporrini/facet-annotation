package it.disco.unimib.labeller.predicates;

public class Constant implements Discriminacy{

	@Override
	public double of(String predicate, String context, double frequencyOfPredicate) throws Exception {
		return 1;
	}
}