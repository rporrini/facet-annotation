package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.List;

public class AnnotationWithType implements AnnotationAlgorithm{
	
	private Annotator annotator;
	private TypeRanker ranker;

	public AnnotationWithType(Annotator annotator, TypeRanker ranker){
		this.annotator = annotator;
		this.ranker = ranker;
	}
	
	@Override
	public AnnotationResult typeOf(List<String> elements) throws Exception {
		List<String> entities = annotator.annotate(elements.toArray(new String[elements.size()]));
		return ranker.typeOf(entities.toArray(new String[entities.size()]));
	}
}