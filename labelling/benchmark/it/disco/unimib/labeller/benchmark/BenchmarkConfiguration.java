package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.AnnotationWithType;
import it.disco.unimib.labeller.labelling.Annotator;
import it.disco.unimib.labeller.labelling.ContextUnaware;
import it.disco.unimib.labeller.labelling.MajorityPredicate;
import it.disco.unimib.labeller.labelling.MaximumLikelihoodPredicate;
import it.disco.unimib.labeller.labelling.TypeRanker;

import java.io.File;

import org.apache.lucene.store.NIOFSDirectory;

public class BenchmarkConfiguration{
	
	private String name;
	private AnnotationAlgorithm algorithm;

	public BenchmarkConfiguration(String name){
		this.name = name;
	}
	
	public String name(){
		return name;
	}
	
	public BenchmarkConfiguration typeAnnotation(Annotator annotator, TypeRanker ranker){
		this.algorithm = new AnnotationWithType(annotator, ranker);
		return this;
	}
	
	public BenchmarkConfiguration predicateAnnotation() throws Exception{
		FullTextSearch fts = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null);
		this.algorithm = new MaximumLikelihoodPredicate(new ContextUnaware(fts));
		return this;
	}
	
	public BenchmarkConfiguration majorityAnnotation(double threshold) throws Exception{
		FullTextSearch fts = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null);
		this.algorithm = new MajorityPredicate(new ContextUnaware(fts), threshold);
		return this;
	}
	
	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
}