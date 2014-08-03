package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.Occurrences;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.Constant;
import it.disco.unimib.labeller.labelling.LogarithmicContextForPredicate;
import it.disco.unimib.labeller.labelling.MajorityHit;
import it.disco.unimib.labeller.labelling.PredicateMaximumLikelihood;
import it.disco.unimib.labeller.labelling.SimpleContextForPredicate;
import it.disco.unimib.labeller.labelling.TopK;

import java.io.File;
import java.util.HashMap;

import org.apache.lucene.store.NIOFSDirectory;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class BenchmarkParameters{
	
	private CommandLineArguments args;

	public BenchmarkParameters(String[] args) {
		this.args = new CommandLineArguments(args);
	}
	
	public Summary analysis(){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("qualitative", new Qualitative());
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEval(algorithmString() + "-" +occurrencesString() + "-" + contextString()));
		return summaries.get(metricString());
	}

	public AnnotationAlgorithm algorithm() throws Exception{
		String knowledgeBase = knowledgeBaseString();
		Occurrences occurrences = occurrences();
		SelectionCriterion context = context();
		
		GroupBySearch index = new GroupBySearch(new NIOFSDirectory(new File(indexPath(knowledgeBase))), occurrences, new IndexFields(knowledgeBase));
		
		HashMap<String, AnnotationAlgorithm> configurations = new HashMap<String, AnnotationAlgorithm>();
		configurations.put("mh", new MajorityHit(index, context, new Constant(), new Constant()));
		configurations.put("mhw", new MajorityHit(index, context, new LogarithmicContextForPredicate(index, new PartialContext()), new Constant()));
		configurations.put("mhsw", new MajorityHit(index, context, new SimpleContextForPredicate(index, new PartialContext()), new Constant()));
		configurations.put("ml", new PredicateMaximumLikelihood(index, context));
		return getAlgorithm(configurations.get(algorithmString()));
	}

	public GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File(goldStandardPath()))).getGroups();
	}
	
	private SelectionCriterion context(){
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		contexts.put("complete", new CompleteContext());
		contexts.put("no", new NoContext());
		contexts.put("partial", new PartialContext());
		return contexts.get(contextString());
	}
	
	private Occurrences occurrences(){
		HashMap<String, Occurrences> occurrences = new HashMap<String, Occurrences>();
		occurrences.put("simple", new SimpleOccurrences());
		occurrences.put("contextualized", new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity())));
		return occurrences.get(occurrencesString());
	}
	
	private String indexPath(String knowledgeBase) {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("dbpedia-with-labels", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("yago1", "../evaluation/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase);
	}
	
	private String goldStandardPath() {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/gold-standard-enhanced");
		paths.put("dbpedia-with-labels", "../evaluation/gold-standard-enhanced");
		paths.put("yago1", "../evaluation/gold-standard-sarawagi-enhanced");
		return paths.get(knowledgeBaseString());
	}
	
	private String algorithmString(){
		return args.asString("algorithm");
	}
	
	private String occurrencesString(){
		return args.asString("occurrences");
	}
	
	private String contextString() {
		return args.asString("context");
	}
	
	private String knowledgeBaseString(){
		return args.asString("kb");
	}
	
	private String metricString(){
		return args.asString("summary");
	}
	
	private AnnotationAlgorithm getAlgorithm(AnnotationAlgorithm algorithm){
		return new TopK(1000, algorithm);
	}
}