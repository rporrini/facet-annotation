package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.Score;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.InspectedPredicates;
import it.disco.unimib.labeller.labelling.MajorityHit;
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
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = maximumLikelihoodAlgorithm(fts);
		return this;
	}

	public BenchmarkConfiguration majorityAnnotation(double threshold, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(new CountPredicates(), query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityPredicate(new InspectedPredicates(new CandidatePredicates(fts)), threshold));
		return this;
	}
	
	public BenchmarkConfiguration majorityHit(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityHit(new InspectedPredicates(new CandidatePredicates(fts))));
		return this;
	}

	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
	
	private GroupBySearch groupBySearchIndex(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		return new GroupBySearch(new NIOFSDirectory(new File(index)), score, query, knowledgeBase);
	}
	
	private MaximumLikelihoodPredicate maximumLikelihoodAlgorithm(Index fts) {
		return new MaximumLikelihoodPredicate(new InspectedPredicates(new CandidatePredicates(fts)));
	}
	
	private TopK majorityAlgorithm(AnnotationAlgorithm algorithmType) {
		return new TopK(1000, algorithmType);
	}
}