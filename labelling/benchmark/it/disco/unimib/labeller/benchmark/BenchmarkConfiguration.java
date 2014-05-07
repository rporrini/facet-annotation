package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.RankingStrategy;
import it.disco.unimib.labeller.index.Score;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.AnnotationWithType;
import it.disco.unimib.labeller.labelling.Annotator;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.MajorityPredicate;
import it.disco.unimib.labeller.labelling.MaximumLikelihoodPredicate;
import it.disco.unimib.labeller.labelling.TopK;
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
	
	public BenchmarkConfiguration predicateAnnotationWithCustomGrouping(Score score, FullTextQuery query) throws Exception{
		Index fts = new GroupBySearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), score, query);
		this.algorithm = new MaximumLikelihoodPredicate(new CandidatePredicates(fts));
		return this;
	}
	
	public BenchmarkConfiguration predicateAnnotation(RankingStrategy ranking, FullTextQuery query) throws Exception{
		Index fts = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null, ranking, query);
		this.algorithm = new MaximumLikelihoodPredicate(new CandidatePredicates(fts));
		return this;
	}
	
	public BenchmarkConfiguration majorityAnnotation(double threshold, FullTextQuery query) throws Exception{
		Index fts = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null, new RankByFrequency(), query);
		this.algorithm = new TopK(1000, new MajorityPredicate(new CandidatePredicates(fts), threshold));
		return this;
	}
	
	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
}