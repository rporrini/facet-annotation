package it.disco.unimib.labeller.labelling;

public interface Discriminacy{
	
	public double of(String predicate, String contextOrValue, double frequencyOfPredicate, Distribution distribution) throws Exception;
}