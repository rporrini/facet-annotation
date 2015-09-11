package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.FullyContextualizedValue;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.properties.DatasetSummary;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class EvaluationResources {
	
	private String evaluationPath;

	public EvaluationResources() {
		this.evaluationPath = "../evaluation";
	}
	
	public EvaluationResources evaluationPath(String path){
		this.evaluationPath = path;
		return this;
	}

	public ScaledDepths hierarchyFrom(String knowledgeBase) throws Exception {
		if(knowledgeBase.startsWith("yago1")){
			return new ScaledDepths(new InputFile(new File(evaluationPath + "/labeller-indexes/yago1/depths/types.csv")));
		}
		if(knowledgeBase.startsWith("dbpedia")){
			return new ScaledDepths(new InputFile(new File(evaluationPath + "/labeller-indexes/dbpedia/depths/types.csv")));
		}
		return null;
	}

	public String indexPath(String knowledgeBase) {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", evaluationPath + "/labeller-indexes/dbpedia/properties");
		paths.put("dbpedia-ontology", evaluationPath + "/labeller-indexes/dbpedia-ontology/properties");
		paths.put("dbpedia-with-labels", evaluationPath + "/labeller-indexes/dbpedia/properties");
		paths.put("yago1", evaluationPath + "/labeller-indexes/yago1/properties");
		paths.put("yago1-simple", evaluationPath + "/labeller-indexes/yago1/properties");
		return paths.get(knowledgeBase);
	}
	
	public String goldStandardPath(String knowledgeBase) throws Exception {
		HashMap<String, String> paths = new HashMap<String, String>();
		paths.put("dbpedia", evaluationPath + "/gold-standards/dbpedia-enhanced");
		paths.put("dbpedia-ontology", evaluationPath + "/gold-standards/dbpedia-enhanced-ontology");
		paths.put("dbpedia-with-labels", evaluationPath + "/gold-standards/dbpedia-enhanced");
		paths.put("yago1", evaluationPath + "/gold-standards/yago1-enhanced");
		paths.put("yago1-simple", evaluationPath + "/gold-standards/yago1-simple");
		return paths.get(knowledgeBase);
	}
	
	public SimilarityMetric occurrences(String type) throws Exception{
		HashMap<String, SimilarityMetric> occurrences = new HashMap<String, SimilarityMetric>();
		occurrences.put("simple", new ConstantSimilarity());
		occurrences.put("contextualized", new SimilarityMetricWrapper(new JaccardSimilarity()));
		return occurrences.get(type);
	}
	
	public SelectionCriterion context(IndexFields fields, String type) throws Exception{
		HashMap<String, SelectionCriterion> contexts = new HashMap<String, SelectionCriterion>();
		
		contexts.put("complete", new FullyContextualizedValue(fields));
		contexts.put("no", new OnlyValue(fields));
		contexts.put("partial", new PartiallyContextualizedValue(fields));
		return contexts.get(type);
	}
	
	public DatasetSummary rangeSummariesFrom(String knowledgeBase) throws Exception {
		String directory = "";
		if(knowledgeBase.startsWith("yago1")){
			directory = evaluationPath + "/yago1-ranges";
		}
		if(knowledgeBase.startsWith("dbpedia")){
			directory = evaluationPath + "/dbpedia-ranges";
		}
		return summaryFrom(directory);
	}
	
	public DatasetSummary domainSummariesFrom(String knowledgeBase) throws Exception {
		String directory = "";
		if(knowledgeBase.startsWith("yago1")){
			directory = evaluationPath + "/yago1-domains";
		}
		if(knowledgeBase.startsWith("dbpedia")){
			directory = evaluationPath + "/dbpedia-domains";
		}
		return summaryFrom(directory);
	}
	
	public DatasetSummary summaryFrom(String directory) throws Exception {
		List<InputFile> summaries = new ArrayList<InputFile>();
		for(File file : new File(directory).listFiles()){
			summaries.add(new InputFile(file));
		}
		return new DatasetSummary(summaries.toArray(new InputFile[summaries.size()]));
	}

}
