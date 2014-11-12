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
	
	private Command command; 

	public BenchmarkParameters(Command command) throws Exception {
		this.command = command;
	}
	
	public Summary analysis() throws Exception{
		HashMap<String, Summary> summaries = new HashMap<String, Summary>();
		summaries.put("questionnaire", new Questionnaire());
		summaries.put("trec", new TrecEvalQrels(algorithmString() + "-" +occurrencesString() + "-" + contextString()));
		return summaries.get(metricString());
	}

	public AnnotationAlgorithm algorithm() throws Exception{
		String knowledgeBase = knowledgeBaseString();
		Occurrences occurrences = occurrences();
		SelectionCriterion context = context();
		
		GroupBySearch index = new GroupBySearch(new NIOFSDirectory(new File(indexPath(knowledgeBase))), occurrences, new IndexFields(knowledgeBase));
		
		HashMap<String, AnnotationAlgorithm> configurations = new HashMap<String, AnnotationAlgorithm>();
		configurations.put("mh", majority(index, context));
		configurations.put("mhw", pfd(index, context));
		configurations.put("mhsw", simplePdf(index, context));
		configurations.put("mhwv", pdfWithValueDiscriminacy(index, context));
		configurations.put("mhwcv", pfdWithValueDiscriminacyAndPredicateDiscriminacy(index, context));
		configurations.put("ml", new PredicateMaximumLikelihood(index, context));
		return getAlgorithm(configurations.get(algorithmString()));
	}

	private MajorityHit pfdWithValueDiscriminacyAndPredicateDiscriminacy(GroupBySearch index, SelectionCriterion context) {
		return new MajorityHit(index, context, new ValueForPredicate(index), new LogarithmicContextForPredicate(index, new PartialContext()));
	}

	private MajorityHit pdfWithValueDiscriminacy(GroupBySearch index, SelectionCriterion context) {
		return new MajorityHit(index, context, new ValueForPredicate(index), new Constant());
	}

	private MajorityHit simplePdf(GroupBySearch index, SelectionCriterion context) {
		return new MajorityHit(index, context, new Constant(), new SimpleContextForPredicate(index, new PartialContext()));
	}

	private MajorityHit pfd(GroupBySearch index, SelectionCriterion context) {
		return new MajorityHit(index, context, new Constant(), new LogarithmicContextForPredicate(index, new PartialContext()));
	}

	private MajorityHit majority(GroupBySearch index, SelectionCriterion context) {
		return new MajorityHit(index, context, new Constant(), new Constant());
	}

	public GoldStandard goldStandard() throws Exception {
		return new OrderedGroups(new UnorderedGroups(new File(goldStandardPath())));
	}
	
	private SelectionCriterion context() throws Exception{
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		contexts.put("complete", new CompleteContext());
		contexts.put("no", new NoContext());
		contexts.put("partial", new PartialContext());
		return contexts.get(contextString());
	}
	
	private Occurrences occurrences() throws Exception{
		HashMap<String, Occurrences> occurrences = new HashMap<String, Occurrences>();
		occurrences.put("simple", new SimpleOccurrences());
		occurrences.put("contextualized", new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity())));
		return occurrences.get(occurrencesString());
	}
	
	private String indexPath(String knowledgeBase) {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/labeller-indexes/dbpedia-ontology/properties");
		paths.put("dbpedia-ontology", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("dbpedia-with-labels", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("yago1", "../evaluation/labeller-indexes/yago1/properties");
		paths.put("yago1-simple", "../evaluation/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase);
	}
	
	private String goldStandardPath() throws Exception {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", "../evaluation/gold-standards/dbpedia-enhanced");
		paths.put("dbpedia-ontology", "../evaluation/gold-standards/dbpedia-enhanced-ontology");
		paths.put("dbpedia-with-labels", "../evaluation/gold-standards/dbpedia-enhanced");
		paths.put("yago1", "../evaluation/gold-standards/yago1-enhanced");
		paths.put("yago1-simple", "../evaluation/gold-standards/yago1-simple");
		return paths.get(knowledgeBaseString());
	}
	
	private String algorithmString() throws Exception{
		return command.argumentAsString("algorithm");
	}
	
	private String occurrencesString() throws Exception{
		return command.argumentAsString("occurrences");
	}
	
	private String contextString() throws Exception {
		return command.argumentAsString("context");
	}
	
	private String knowledgeBaseString() throws Exception{
		return command.argumentAsString("kb");
	}
	
	private String metricString() throws Exception{
		return command.argumentAsString("summary");
	}
	
	private AnnotationAlgorithm getAlgorithm(AnnotationAlgorithm algorithm){
		return new TopK(1000, algorithm);
	}
}