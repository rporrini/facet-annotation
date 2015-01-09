package it.disco.unimib.labeller.index;


public class MatchingProperty implements TripleFilter {

	private String property;

	public MatchingProperty(String property) {
		this.property = property;
	}

	@Override
	public boolean matches(NTriple triple) {
		return triple.property().uri().equals(property);
	}
}
