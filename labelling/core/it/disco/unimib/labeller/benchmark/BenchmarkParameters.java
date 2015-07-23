package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.FullyContextualizedValue;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.IndexResultInspection;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.index.TypeConsistency;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;
import it.disco.unimib.labeller.properties.MajorityOverFrequencyOfProperties;
import it.disco.unimib.labeller.properties.PropertyContextSpecificity;
import it.disco.unimib.labeller.properties.PropertyMaximumLikelihood;
import it.disco.unimib.labeller.properties.PropertyTypesConditionalEntropy;
import it.disco.unimib.labeller.properties.TopK;
import it.disco.unimib.labeller.properties.WeightedFrequencyCoverageAndSpecificity;

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
		IndexFields fields = new IndexFields(knowledgeBase);
		
		SelectionCriterion context = context(fields);
		Index index = new IndexResultInspection(new ContextualizedEvidence(new NIOFSDirectory(new File(indexPath(knowledgeBase))), occurrences(), fields));
		
		String algorithm = algorithmString();
		AnnotationAlgorithm algorithmToRun = null;
		if(algorithm.equals("mh")) algorithmToRun = majority(index, context);
		if(algorithm.equals("mhw")) algorithmToRun = pfd(hierarchyFrom(knowledgeBase), index, context, fields);
		if(algorithm.equals("mhw-e")) algorithmToRun = pfdEntropy(hierarchyFrom(knowledgeBase), index, context, fields);
		if(algorithm.equals("ml")) algorithmToRun = maximumLikelihood(index, context);
		
		return topK(algorithmToRun);
	}

	private AnnotationAlgorithm maximumLikelihood(Index index, SelectionCriterion context) {
		return new PropertyMaximumLikelihood(index, context);
	}

	private AnnotationAlgorithm pfd(TypeConsistency depth, Index index, SelectionCriterion context, IndexFields fields) throws Exception {
		return new WeightedFrequencyCoverageAndSpecificity(depth, index, context, new PropertyContextSpecificity(index, fields));
	}
	
	private AnnotationAlgorithm pfdEntropy(TypeConsistency depth, Index index, SelectionCriterion context, IndexFields fields) throws Exception {
		return new WeightedFrequencyCoverageAndSpecificity(depth, index, context, new PropertyTypesConditionalEntropy(index, fields));
	}

	private AnnotationAlgorithm majority(Index index, SelectionCriterion context) {
		return new MajorityOverFrequencyOfProperties(index, context);
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
	
	public SelectionCriterion context(IndexFields fields) throws Exception{
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		
		contexts.put("complete", new FullyContextualizedValue(fields));
		contexts.put("no", new OnlyValue(fields));
		contexts.put("partial", new PartiallyContextualizedValue(fields));
		return contexts.get(contextString());
	}
	
	public SimilarityMetric occurrences() throws Exception{
		HashMap<String, SimilarityMetric> occurrences = new HashMap<String, SimilarityMetric>();
		occurrences.put("simple", new ConstantSimilarity());
		occurrences.put("contextualized", new SimilarityMetricWrapper(new JaccardSimilarity()));
		return occurrences.get(occurrencesString());
	}
	
	public String indexPath(String knowledgeBase) {
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