package it.disco.unimib.index;

import com.hp.hpl.jena.graph.Triple;

public class MatchingPredicate implements TripleFilter {

	private String predicate;

	public MatchingPredicate(String predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean matches(Triple triple) {
		return triple.getPredicate().getURI().equals(predicate);
	}
}
