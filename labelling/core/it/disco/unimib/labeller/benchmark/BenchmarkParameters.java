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
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.Constant;
import it.disco.unimib.labeller.predicates.LogarithmicContextForPredicate;
import it.disco.unimib.labeller.predicates.MajorityHit;
import it.disco.unimib.labeller.predicates.PredicateMaximumLikelihood;
import it.disco.unimib.labeller.predicates.SimpleContextForPredicate;
import it.disco.unimib.labeller.predicates.TopK;
import it.disco.unimib.labeller.predicates.ValueForPredicate;

import java.io.File;
import java.util.HashMap;

import org.apache.lucene.store.NIOFSDirectory;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class BenchmarkParameters{
	
	private CommandLineArguments args;

	public BenchmarkParameters(String[] args) throws Exception {
		this.args = new CommandLineArguments(args);
	}
	
	public Summary analysis(){
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
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
		configurations.put("mhwv", new MajorityHit(index, context, new Constant(), new ValueForPredicate(index)));
		configurations.put("mhwcv", new MajorityHit(index, context, new LogarithmicContextForPredicate(index, new PartialContext()), new ValueForPredicate(index)));
		configurations.put("ml", new PredicateMaximumLikelihood(index, context));
		return getAlgorithm(configurations.get(algorithmString()));
	}

	public GoldStandard goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File(goldStandardPath())));
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
		paths.put("yago1-simple", "../evaluation/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase);
	}
	
	private String goldStandardPath() {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/gold-standards/dbpedia-enhanced");
		paths.put("dbpedia-with-labels", "../evaluation/gold-standards/dbpedia-enhanced");
		paths.put("yago1", "../evaluation/gold-standards/yago1-enhanced");
		paths.put("yago1-simple", "../evaluation/gold-standards/yago1-simple");
		return paths.get(knowledgeBaseString());
	}
	
	private String algorithmString(){
		return args.asString("algorithm").get(0);
	}
	
	private String occurrencesString(){
		return args.asString("occurrences").get(0);
	}
	
	private String contextString() {
		return args.asString("context").get(0);
	}
	
	private String knowledgeBaseString(){
		return args.asString("kb").get(0);
	}
	
	private String metricString(){
		return args.asString("summary").get(0);
	}
	
	private AnnotationAlgorithm getAlgorithm(AnnotationAlgorithm algorithm){
		return new TopK(1000, algorithm);
	}
}