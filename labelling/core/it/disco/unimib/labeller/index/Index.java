package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<AnnotationResult> get(String type, String context) throws Exception;
}