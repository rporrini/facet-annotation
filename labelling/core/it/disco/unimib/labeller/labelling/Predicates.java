package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.FullTextQuery;

import java.util.HashMap;
import java.util.List;

public interface Predicates {

	public HashMap<String, List<AnnotationResult>> forValues(String context, String[] values, FullTextQuery query) throws Exception;

}