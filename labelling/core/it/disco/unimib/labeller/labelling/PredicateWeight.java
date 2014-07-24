package it.disco.unimib.labeller.labelling;

public interface PredicateWeight{
	
	public double discriminacy(String predicate, String context, double frequencyOfPredicate) throws Exception;
}