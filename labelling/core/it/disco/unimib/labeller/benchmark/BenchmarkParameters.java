package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FullyContextualizedValue;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.TypeConsistency;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.MajorityOverFrequencyOfPredicates;
import it.disco.unimib.labeller.predicates.PredicateContextSpecificity;
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
		SelectionCriterion context = context();
		ContextualizedEvidence index = new ContextualizedEvidence(new NIOFSDirectory(new File(indexPath(knowledgeBase))), occurrences(), new IndexFields(knowledgeBase));
		
		String algorithm = algorithmString();
		AnnotationAlgorithm algorithmToRun = null;
		if(algorithm.equals("mh")) algorithmToRun = majority(index, context);
		if(algorithm.equals("mhw")) algorithmToRun = pfd(hierarchyFrom(knowledgeBase), index, context);
		if(algorithm.equals("ml")) algorithmToRun = maximumLikelihood(index, context);
		
		return topK(algorithmToRun);
	}

	private AnnotationAlgorithm maximumLikelihood(ContextualizedEvidence index, SelectionCriterion context) {
		return new PredicateMaximumLikelihood(index, context);
	}

	private AnnotationAlgorithm pfd(TypeConsistency depth, ContextualizedEvidence index, SelectionCriterion context) throws Exception {
		return new WeightedFrequencyCoverageAndSpecificity(depth, index, context, new PredicateContextSpecificity(index, new IndexFields(knowledgeBaseString())));
	}

	private AnnotationAlgorithm majority(ContextualizedEvidence index, SelectionCriterion context) {
		return new MajorityOverFrequencyOfPredicates(index, context);
	}

	private TypeConsistency hierarchyFrom(String knowledgeBase) throws Exception {
		if(knowledgeBase.startsWith("yago1")){
			return new ScaledDepths(new InputFile(new File("../evaluation/labeller-indexes/yago1/depths/types.csv")));
		}
		if(knowledgeBase.startsWith("dbpedia")){
			return new ScaledDepths(new InputFile(new File("../evaluation/labeller-indexes/dbpedia/depths/types.csv")));
		}
		return null;
	}
	
	public GoldStandard goldStandard() throws Exception {
		return new OrderedFacets(new UnorderedFacets(new File(goldStandardPath())));
	}
	
	private SelectionCriterion context() throws Exception{
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		
		contexts.put("complete", new FullyContextualizedValue(new IndexFields(knowledgeBaseString())));
		contexts.put("no", new OnlyValue(new IndexFields(knowledgeBaseString())));
		contexts.put("partial", new PartiallyContextualizedValue(new IndexFields(knowledgeBaseString())));
		return contexts.get(contextString());
	}
	
	private SimilarityMetric occurrences() throws Exception{
		HashMap<String, SimilarityMetric> occurrences = new HashMap<String, SimilarityMetric>();
		occurrences.put("simple", new ConstantSimilarity());
		occurrences.put("contextualized", new SimilarityMetricWrapper(new JaccardSimilarity()));
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
	
	private AnnotationAlgorithm topK(AnnotationAlgorithm algorithm){
		return new TopK(1000, algorithm);
	}
}