package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.Occurrences;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.PredicateContextualizedMaximumLikelihood;
import it.disco.unimib.labeller.labelling.CandidatePredicatesReport;
import it.disco.unimib.labeller.labelling.MajorityHit;
import it.disco.unimib.labeller.labelling.Majority;
import it.disco.unimib.labeller.labelling.PredicateMaximumLikelihood;
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
	
	public BenchmarkConfiguration predicateAnnotationWithContextualMaximumLikelihood(Occurrences score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = contextualizedMaximumLikelihoodAlgorithm(fts);
		return this;
	}
	
	public BenchmarkConfiguration predicateAnnotationWithCustomGrouping(Occurrences score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = maximumLikelihoodAlgorithm(fts, query);
		return this;
	}

	public BenchmarkConfiguration majorityAnnotation(double threshold, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(new SimpleOccurrences(), query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new Majority(new CandidatePredicatesReport(new CandidatePredicates(fts)), threshold, query));
		return this;
	}
	
	public BenchmarkConfiguration weightedMajorityHit(Occurrences score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityHit(fts, new ContextForPredicate(fts), new ValueForPredicate())); 
		return this;
	}
	
	public BenchmarkConfiguration majorityHit(Occurrences score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		Index fts = groupBySearchIndex(score, query, index, knowledgeBase);
		this.algorithm = majorityAlgorithm(new MajorityHit(fts, new Constant(), new Constant()));
		return this;
	}

	public AnnotationAlgorithm getAlgorithm(){
		return algorithm;
	}
	
	private GroupBySearch groupBySearchIndex(Occurrences score, FullTextQuery query, String index, KnowledgeBase knowledgeBase) throws Exception{
		return new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
	}
	
	private AnnotationAlgorithm contextualizedMaximumLikelihoodAlgorithm(GroupBySearch fts) {
		return new PredicateContextualizedMaximumLikelihood(fts);
	}
	
	private PredicateMaximumLikelihood maximumLikelihoodAlgorithm(Index fts, FullTextQuery query) {
		return new PredicateMaximumLikelihood(new CandidatePredicatesReport(new CandidatePredicates(fts)), query);
	}
	
	private TopK majorityAlgorithm(AnnotationAlgorithm algorithmType) {
		return new TopK(1000, algorithmType);
	}
}