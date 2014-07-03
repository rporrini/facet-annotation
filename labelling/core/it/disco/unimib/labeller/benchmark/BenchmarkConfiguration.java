package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.Score;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.InspectedPredicates;
import it.disco.unimib.labeller.labelling.MajorityPredicate;
import it.disco.unimib.labeller.labelling.MaximumLikelihoodPredicate;
import it.disco.unimib.labeller.labelling.TopK;

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
	
	public BenchmarkConfiguration predicateAnnotationWithCustomGrouping(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, query, knowledgeBase);
		this.algorithm = new MaximumLikelihoodPredicate(new InspectedPredicates(new CandidatePredicates(fts)));
		return this;
	}
	
	public BenchmarkConfiguration majorityAnnotation(double threshold, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = new FullTextSearch(new NIOFSDirectory(new File(index)), null, null, new RankByFrequency(), query, knowledgeBase);
		this.algorithm = new TopK(1000, new MajorityPredicate(new InspectedPredicates(new CandidatePredicates(fts)), threshold));
		return this;
	}
	
	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
}