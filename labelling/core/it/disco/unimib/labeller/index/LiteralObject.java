package it.disco.unimib.labeller.index;

public class LiteralObject implements TripleFilter{

	@Override
	public boolean matches(NTriple triple) throws Exception {
		return !triple.object().uri().startsWith("http:");
	}
}
