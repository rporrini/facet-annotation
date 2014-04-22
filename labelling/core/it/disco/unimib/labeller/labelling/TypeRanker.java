package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

public interface TypeRanker {

	public AnnotationResult typeOf(String... entity) throws Exception;
}