package it.disco.unimib.benchmark;

import it.disco.unimib.labelling.Annotator;
import it.disco.unimib.labelling.TypeRanker;

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
	
	public Annotator annotator(){
		return annotator;
	}
	
	public BenchmarkConfiguration withRanker(TypeRanker ranker){
		this.ranker = ranker;
		return this;
	}
	
	public TypeRanker ranker(){
		return ranker;
	}
	
	public String name(){
		return name;
	}
}