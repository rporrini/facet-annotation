package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<AnnotationResult> get(String value, String context) throws Exception;
}