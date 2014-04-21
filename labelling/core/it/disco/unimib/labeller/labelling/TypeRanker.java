package it.disco.unimib.labeller.labelling;

public interface TypeRanker {

	public String typeOf(String... entity) throws Exception;
}