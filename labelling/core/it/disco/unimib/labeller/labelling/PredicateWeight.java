package it.disco.unimib.labeller.labelling;

public interface PredicateWeight{
	
	public double discriminacy(String predicate, String contextOrValue, double frequencyOfPredicate, Distribution distribution) throws Exception;
}