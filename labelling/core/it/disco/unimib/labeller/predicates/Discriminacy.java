package it.disco.unimib.labeller.predicates;

public interface Discriminacy{
	
	public double of(String predicate, String contextOrValue, double frequencyOfPredicate) throws Exception;
}