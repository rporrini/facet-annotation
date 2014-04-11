package it.disco.unimib.labeller.index;


public class MatchingPredicate implements TripleFilter {

	private String predicate;

	public MatchingPredicate(String predicate) {
		this.predicate = predicate;
	}

	@Override
	public boolean matches(NTriple triple) {
		return triple.predicate().equals(predicate);
	}
}
