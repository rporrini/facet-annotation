package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.AnnotationWithType;
import it.disco.unimib.labeller.labelling.Annotator;
import it.disco.unimib.labeller.labelling.TypeRanker;

public class BenchmarkConfiguration{
	
	private Annotator annotator;
	private TypeRanker ranker;
	private String name;

	public BenchmarkConfiguration(String name){
		this.name = name;
	}
	
	public BenchmarkConfiguration withAnnotator(Annotator annotator){
		this.annotator = annotator;
		return this;
	}
	
	public BenchmarkConfiguration withRanker(TypeRanker ranker){
		this.ranker = ranker;
		return this;
	}
	
	public String name(){
		return name;
	}
	
	public AnnotationAlgorithm typeAnnotation(){
		return new AnnotationWithType(annotator, ranker);
	}
}