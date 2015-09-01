package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.TypeConsistency;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;
import it.disco.unimib.labeller.properties.DomainAndRangeConsistency;
import it.disco.unimib.labeller.properties.MajorityOverFrequencyOfProperties;
import it.disco.unimib.labeller.properties.PropertyContextSpecificity;
import it.disco.unimib.labeller.properties.PropertyMaximumLikelihood;
import it.disco.unimib.labeller.properties.PropertyTypesConditionalEntropy;
import it.disco.unimib.labeller.properties.TopK;
import it.disco.unimib.labeller.properties.WeightedFrequencyCoverageAndSpecificity;

import java.io.File;
import java.util.HashMap;

import org.apache.lucene.store.NIOFSDirectory;

public class BenchmarkParameters{
	
	private Command command;
	private EvaluationResources evaluationResources; 

	public BenchmarkParameters(Command command) throws Exception {
		this.command = command;
		this.evaluationResources = new EvaluationResources();
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
		
		SelectionCriterion context = evaluationResources.context(fields, contextString());
		Index index = new ContextualizedEvidence(new NIOFSDirectory(new File(evaluationResources.indexPath(knowledgeBase)).toPath()), evaluationResources.occurrences(occurrencesString()), fields);
		
		String algorithm = algorithmString();
		AnnotationAlgorithm algorithmToRun = null;
		if(algorithm.equals("mh")) algorithmToRun = majority(index, context);
		if(algorithm.equals("mhw")) algorithmToRun = pfd(evaluationResources.hierarchyFrom(knowledgeBase), index, context, fields);
		if(algorithm.equals("mhw-e")) algorithmToRun = pfdEntropy(evaluationResources.hierarchyFrom(knowledgeBase), index, context, fields);
		if(algorithm.equals("drc")) algorithmToRun = domainAndRangeConsistency(knowledgeBase, context, index);
		if(algorithm.equals("ml")) algorithmToRun = maximumLikelihood(index, context);
		
		return topK(algorithmToRun);
	}

	private DomainAndRangeConsistency domainAndRangeConsistency(String knowledgeBase, SelectionCriterion context, Index index) throws Exception {
		return new DomainAndRangeConsistency(index, context, evaluationResources.domainSummariesFrom(knowledgeBase), evaluationResources.rangeSummariesFrom(knowledgeBase), evaluationResources.hierarchyFrom(knowledgeBase));
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

	public GoldStandard goldStandard() throws Exception {
		return new OrderedFacets(new UnorderedFacets(new File(evaluationResources.goldStandardPath(knowledgeBaseString()))));
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