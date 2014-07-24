package it.disco.unimib.labeller.labelling;

public class PredicateWithoutWeight implements PredicateWeight{

	@Override
	public double discriminacy(String predicate, String context, double frequencyOfPredicate, Distribution distribution) throws Exception {
		return 1;
	}
	
}