package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.HashMap;
import java.util.List;

public interface Predicates {

	public HashMap<String, List<AnnotationResult>> forValues(String context, String[] values) throws Exception;

}