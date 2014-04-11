package it.disco.unimib.labeller.index;


public interface TripleFilter {

	public boolean matches(NTriple triple) throws Exception;

}