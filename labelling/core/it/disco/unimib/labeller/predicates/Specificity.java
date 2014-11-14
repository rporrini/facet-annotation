package it.disco.unimib.labeller.predicates;

public interface Specificity{
	
	public double of(String predicate, String context, double frequencyOfPredicate) throws Exception;
}