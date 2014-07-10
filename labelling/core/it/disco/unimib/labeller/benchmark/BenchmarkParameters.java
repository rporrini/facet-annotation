package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.DistanceMetricWrapper;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.WeightedPredicates;

import java.io.File;
import java.util.HashMap;

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

	public BenchmarkConfiguration configuration() throws Exception{
		HashMap<String, BenchmarkConfiguration> configurations = new HashMap<String, BenchmarkConfiguration>();
		configurations.put("majority", new BenchmarkConfiguration("majority").majorityAnnotation(majorityK(), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		configurations.put("majority-hit", new BenchmarkConfiguration("majority-hit").majorityHit(new CountPredicates(), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		configurations.put("majority-hit-jaccard", new BenchmarkConfiguration("majority-hit-jaccard").majorityHit(new WeightedPredicates(new SimilarityMetricWrapper(new JaccardSimilarity())), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		configurations.put("ml-frequency", new BenchmarkConfiguration("ml-frequency").predicateAnnotationWithCustomGrouping(new CountPredicates(), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		configurations.put("ml-jaccard", new BenchmarkConfiguration("ml-jaccard").predicateAnnotationWithCustomGrouping(new WeightedPredicates(new SimilarityMetricWrapper(new JaccardSimilarity())), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		configurations.put("ml-ngram", new BenchmarkConfiguration("ml-ngram").predicateAnnotationWithCustomGrouping(new WeightedPredicates(new DistanceMetricWrapper(new QGramsDistance())), context(), indexPath(), new KnowledgeBase(knowledgeBase())));
		return configurations.get(algorithm());
	}

	public GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File(goldStandardPath()))).getGroups();
	}
	
	private FullTextQuery context(){
		HashMap<String, FullTextQuery> contexts = new HashMap<String, FullTextQuery>();
		contexts.put("with-context", new MandatoryContext());
		contexts.put("without-context", new OptionalContext());
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
}