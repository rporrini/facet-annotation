package it.disco.unimib.labeller.index;

public interface WriteStore {

	public WriteStore add(NTriple triple) throws Exception;
}