package it.disco.unimib.labeller.labelling;

public class Constant implements Discriminacy{

	@Override
	public double of(String predicate, String context, double frequencyOfPredicate) throws Exception {
		return 1;
	}
}