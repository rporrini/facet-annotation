package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.Occurrences;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.LogarithmicPredicateSpecificy;
import it.disco.unimib.labeller.predicates.MajorityOverFrequencyOfPredicates;
import it.disco.unimib.labeller.predicates.PredicateMaximumLikelihood;
import it.disco.unimib.labeller.predicates.TopK;
import it.disco.unimib.labeller.predicates.WeightedFrequencyCoverageAndSpecificity;

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
		TripleSelectionCriterion context = context();
		ContextualizedEvidence index = new ContextualizedEvidence(new NIOFSDirectory(new File(indexPath(knowledgeBase))), occurrences(), new IndexFields(knowledgeBase));
		
		HashMap<String, AnnotationAlgorithm> configurations = new HashMap<String, AnnotationAlgorithm>();
		configurations.put("mh", majority(index, context));
		configurations.put("mhw", pfd(index, context));
		configurations.put("ml", maximumLikelihood(index, context));
		return getAlgorithm(configurations.get(algorithmString()));
	}

	private AnnotationAlgorithm maximumLikelihood(ContextualizedEvidence index, TripleSelectionCriterion context) {
		return new PredicateMaximumLikelihood(index, context);
	}

	private AnnotationAlgorithm pfd(ContextualizedEvidence index, TripleSelectionCriterion context) {
		return new WeightedFrequencyCoverageAndSpecificity(index, context, new LogarithmicPredicateSpecificy(index));
	}

	private AnnotationAlgorithm majority(ContextualizedEvidence index, TripleSelectionCriterion context) {
		return new MajorityOverFrequencyOfPredicates(index, context);
	}

	public GoldStandard goldStandard() throws Exception {
		return new OrderedFacets(new UnorderedFacets(new File(goldStandardPath())));
	}
	
	private TripleSelectionCriterion context() throws Exception{
		HashMap<String, TripleSelectionCriterion> contexts = new HashMap<String, TripleSelectionCriterion>();
		AllValues valueMatching = new AllValues();
		
		contexts.put("complete", new CompleteContext(valueMatching));
		contexts.put("no", new NoContext(valueMatching));
		contexts.put("partial", new PartialContext(valueMatching));
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
		paths.put("dbpedia", "../evaluation/labeller-indexes/dbpedia/properties");
		paths.put("dbpedia-ontology", "../evaluation/labeller-indexes/dbpedia-ontology/properties");
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