package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.Score;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.ContextualizedMaximumLikelihood;
import it.disco.unimib.labeller.labelling.InspectedPredicates;
import it.disco.unimib.labeller.labelling.MajorityHit;
import it.disco.unimib.labeller.labelling.MajorityPredicate;
import it.disco.unimib.labeller.labelling.MaximumLikelihoodPredicate;
import it.disco.unimib.labeller.labelling.ContextForPredicate;
import it.disco.unimib.labeller.labelling.ValueForPredicate;
import it.disco.unimib.labeller.labelling.Constant;
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
	
	public BenchmarkConfiguration predicateAnnotationWithContextualMaximumLikelihood(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = contextualizedMaximumLikelihoodAlgorithm(fts);
		return this;
	}
	
	public BenchmarkConfiguration predicateAnnotationWithCustomGrouping(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = maximumLikelihoodAlgorithm(fts, query);
		return this;
	}

	public BenchmarkConfiguration majorityAnnotation(double threshold, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(new CountPredicates(), query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityPredicate(new InspectedPredicates(new CandidatePredicates(fts)), threshold, query));
		return this;
	}
	
	public BenchmarkConfiguration weightedMajorityHit(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityHit(fts, new ContextForPredicate(fts), new ValueForPredicate())); 
		return this;
	}
	
	public BenchmarkConfiguration majorityHit(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityHit(fts, new Constant(), new Constant()));
		return this;
	}

	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
	
	private GroupBySearch groupBySearchIndex(Score score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		return new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
	}
	
	private AnnotationAlgorithm contextualizedMaximumLikelihoodAlgorithm(GroupBySearch fts) {
		return new ContextualizedMaximumLikelihood(fts);
	}
	
	private MaximumLikelihoodPredicate maximumLikelihoodAlgorithm(Index fts, FullTextQuery query) {
		return new MaximumLikelihoodPredicate(new InspectedPredicates(new CandidatePredicates(fts)), query);
	}
	
	private TopK majorityAlgorithm(AnnotationAlgorithm algorithmType) {
		return new TopK(1000, algorithmType);
	}
}