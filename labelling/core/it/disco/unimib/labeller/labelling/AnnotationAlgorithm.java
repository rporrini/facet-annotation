package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.List;

public interface AnnotationAlgorithm {

	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception;
}