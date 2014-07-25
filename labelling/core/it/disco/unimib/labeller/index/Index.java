package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<AnnotationResult> get(String value, String context, FullTextQuery query) throws Exception;

	public long count(String predicate, String context, FullTextQuery query) throws Exception;
}