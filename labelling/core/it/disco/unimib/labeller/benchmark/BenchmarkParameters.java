package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.DistanceMetricWrapper;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.Occurrences;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.Constant;
import it.disco.unimib.labeller.labelling.ContextForPredicate;
import it.disco.unimib.labeller.labelling.Majority;
import it.disco.unimib.labeller.labelling.MajorityHit;
import it.disco.unimib.labeller.labelling.PredicateContextualizedMaximumLikelihood;
import it.disco.unimib.labeller.labelling.PredicateMaximumLikelihood;
import it.disco.unimib.labeller.labelling.TopK;
import it.disco.unimib.labeller.labelling.ValueForPredicate;

import java.io.File;
import java.util.HashMap;

import org.apache.lucene.store.NIOFSDirectory;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;

public class BenchmarkParameters{
	
	private String[] args;

	public BenchmarkParameters(String[] args) {
		this.args = args;
	}
	
	public Summary analysis(){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("qualitative", new Qualitative());
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEval(algorithm() + "-" + contextString() + threshold()));
		return summaries.get(metricName());
	}

	public AnnotationAlgorithm configuration() throws Exception{
		HashMap<String, AnnotationAlgorithm> configurations = new HashMap<String, AnnotationAlgorithm>();
		configurations.put("majority", majorityAnnotation(majorityK(), context(), new SimpleOccurrences(), indexPath(), getKB()));
		configurations.put("majority-hit", majorityHit(context(), new SimpleOccurrences(), indexPath(), getKB()));
		configurations.put("majority-hit-jaccard", majorityHit(context(), new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity())), indexPath(), getKB()));
		configurations.put("majority-hit-weighted", weightedMajorityHit(context(), new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity())), indexPath(), getKB()));
		configurations.put("ml-frequency", maximumLikelihood(context(), new SimpleOccurrences(), indexPath(), getKB()));
		configurations.put("ml-jaccard", maximumLikelihood(context(), new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity())), indexPath(), getKB()));
		configurations.put("cml", contextualizedMaximumLikelihood(new SimpleOccurrences(), indexPath(), getKB()));
		configurations.put("ml-ngram", maximumLikelihood(context(), new ContextualizedOccurrences(new DistanceMetricWrapper(new QGramsDistance())), indexPath(), getKB()));
		return getAlgorithm(configurations.get(algorithm()));
	}

	public GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File(goldStandardPath()))).getGroups();
	}
	
	private SelectionCriterion context(){
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		contexts.put("with-context", new CompleteContext());
		contexts.put("without-context", new NoContext());
		contexts.put("with-partial-context", new PartialContext());
		return contexts.get(contextString());
	}
	
	private String indexPath() {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("dbpedia-with-labels", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("yago1", "../evaluation/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase());
	}
	
	private String goldStandardPath() {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/gold-standard-enhanced");
		paths.put("dbpedia-with-labels", "../evaluation/gold-standard-enhanced");
		paths.put("yago1", "../evaluation/gold-standard-sarawagi-enhanced");
		return paths.get(knowledgeBase());
	}
	
	private String threshold() {
		String threshold = majorityK() + "";
		if(threshold.equals("0.0")) return "";
		return threshold;
	}
	
	private KnowledgeBase getKB() {
		return new KnowledgeBase(knowledgeBase());
	}
	
	private String knowledgeBase(){
		return args[0];
	}
	
	private String algorithm(){
		return args[1];
	}
	
	private String metricName(){
		return args[2];
	}
	
	private String contextString() {
		return args[3];
	}
	
	private double majorityK() {
		return Double.parseDouble(args.length > 4 ? args[4] : "0");
	}
	
	private AnnotationAlgorithm contextualizedMaximumLikelihood(Occurrences score, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
		return new PredicateContextualizedMaximumLikelihood(fts);
	}
	
	private AnnotationAlgorithm maximumLikelihood(SelectionCriterion query, Occurrences score, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
		return  new PredicateMaximumLikelihood(fts, query);
	}

	private AnnotationAlgorithm majorityAnnotation(double threshold, SelectionCriterion query, Occurrences score, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
		return new Majority(fts, threshold, query);
	}
	
	private AnnotationAlgorithm weightedMajorityHit(SelectionCriterion query, Occurrences score, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
		return new MajorityHit(fts, new ContextForPredicate(fts), new ValueForPredicate());
	}
	
	private AnnotationAlgorithm majorityHit(SelectionCriterion query, Occurrences score, String index, KnowledgeBase knowledgeBase) throws Exception{
		GroupBySearch fts = new GroupBySearch(new NIOFSDirectory(new File(index)), score, knowledgeBase);
		return new MajorityHit(fts, new Constant(), new Constant());
	}

	private AnnotationAlgorithm getAlgorithm(AnnotationAlgorithm algorithm){
		return new TopK(1000, algorithm);
	}
}