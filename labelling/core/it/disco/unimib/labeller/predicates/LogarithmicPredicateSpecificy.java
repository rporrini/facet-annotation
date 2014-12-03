package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.Index;

public class LogarithmicPredicateSpecificy implements Specificity{
	
	private Specificity specificity;
	
	public LogarithmicPredicateSpecificy(Index index) {
		this.specificity = new SimplePredicateSpeficity(index);
	}
	
	@Override
	public double of(String predicate, String domain) throws Exception{
		return Math.log(specificity.of(predicate, domain) + 1.1);
	}
}