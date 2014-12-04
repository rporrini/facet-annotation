package it.disco.unimib.labeller.predicates;

public interface Specificity{
	
	public double of(String predicate, String domain) throws Exception;
}