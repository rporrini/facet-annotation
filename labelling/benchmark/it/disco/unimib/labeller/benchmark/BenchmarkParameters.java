package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.WeightedPredicates;

import java.io.File;
import java.util.HashMap;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class BenchmarkParameters{
	
	private String[] args;

	public BenchmarkParameters(String[] args) {
		this.args = args;
	}
	
	public Summary analysis(){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("qualitative", new Qualitative());
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEval(algorithm()));
		return summaries.get(metricName());
	}

	public BenchmarkConfiguration configuration() throws Exception{
		HashMap<String, BenchmarkConfiguration> configurations = new HashMap<String, BenchmarkConfiguration>();
		configurations.put("majority", new BenchmarkConfiguration("majority").majorityAnnotation(majorityK(), context(), indexPath()));
		configurations.put("ml-frequency", new BenchmarkConfiguration("ml-frequency").predicateAnnotationWithCustomGrouping(new CountPredicates(), context(), indexPath()));
		configurations.put("ml-jaccard", new BenchmarkConfiguration("ml-jaccard").predicateAnnotationWithCustomGrouping(new WeightedPredicates(new JaccardSimilarity()), context(), indexPath()));
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
		paths.put("yago1", "../evaluation/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase());
	}
	
	private String goldStandardPath() {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/gold-standard-enhanced");
		paths.put("yago1", "../evaluation/gold-standard-sarawagi-enhanced");
		return paths.get(knowledgeBase());
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