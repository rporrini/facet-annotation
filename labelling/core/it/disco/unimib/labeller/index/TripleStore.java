package it.disco.unimib.labeller.index;

public interface TripleStore {

	public TripleStore add(NTriple triple) throws Exception;
}