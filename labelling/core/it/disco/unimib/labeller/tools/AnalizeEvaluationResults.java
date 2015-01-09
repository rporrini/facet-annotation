package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AnalizeEvaluationResults {

	public static void main(String[] args) throws Exception {
		
		Command command = new Command()
						.withArgument("m", "the considered metric, from trec_eval")
						.withArgument("kb", "the knowledge base for which results are analyzed, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
						.withArgument("alg", "the algorithm whose results are analyzed")
						.withArgument("k", "restrict the analisys to the top k results")
						.withArgument("t", "the threshold under which a result is considered to be improvable")
						.withArgument("v", "toggle in dept analysis")
						.parse(args);
		
		boolean inDeptAnalysis = Boolean.parseBoolean(command.argumentAsString("v"));
		int topK = Integer.parseInt(command.argumentAsString("k"));
		String kb = command.argumentAsString("kb");
		String alg = command.argumentAsString("alg");
		
		double threshold = Double.parseDouble(command.argumentAsString("t"));
		String goldStandard = goldStandardQRels(kb);
		String qrels = resultDirectory(kb) + alg;
		GoldStandard goldStandardGroups = new BenchmarkParameters(command).goldStandard();
		
		List<String> measures = command.argumentsAsStrings("m");
		
		for(String measure : measures){
			if(inDeptAnalysis){
				System.out.println("gold standard: " + goldStandard);
				System.out.println("results: " + qrels);
			}
			
			List<String> results = TrecEval.singleAndSummary(topK, goldStandard, qrels, measure);
			System.out.println(measure + ": " + measureResult(results.get(results.size() - 1)));
			
			if(inDeptAnalysis){
				List<String> notPerfectResults = new ArrayList<String>();
				List<String> particularResults = results.subList(0, results.size() - 1);
				for (String line : particularResults) {
					if(measureResult(line) < threshold) {
						notPerfectResults.add(line);
					}
				}
				double incorrect = (double)notPerfectResults.size();
				double total = (double)particularResults.size();
				System.out.println(incorrect + " improvable groups over " + total + " (" + measure + " < " + threshold + ")");
				
				for(String result : notPerfectResults){
					int id = id(result);
					System.out.println("------------------------------------------");
					GoldStandardFacet groupById = goldStandardGroups.getGroupById(id);
					System.out.println(measure + ": " + measureResult(result));
					System.out.println("ID: " + id + " TYPE LABEL: " + groupById.context() + " (" + groupById.elements().size() + " elements)");
					System.out.println(groupById.elements().subList(0, Math.min(10, groupById.elements().size())) + " ... ");
					System.out.println("EXPECTED PROPERTIES (rel. judgement)\tACTUAL PROPERTIES (score)");
					
					List<TrecGoldStandardProperty> goldStandardProperties = getGoldStandardProperties(goldStandard, topK, id);
					List<TrecResultProperty> resultingProperties = getResultingProperties(qrels, topK, id);
					
					for(int i=0; i < Math.max(goldStandardProperties.size(), resultingProperties.size()); i++){
						String line = "";
						if(i < goldStandardProperties.size()) line+=goldStandardProperties.get(i);
						line+="\t";
						if(i < resultingProperties.size()) line+=resultingProperties.get(i);
						System.out.println(line);
					}
				}
			}
		}
	}

	private static List<TrecResultProperty> getResultingProperties(String goldStandard, int topK, int id) throws Exception {
		List<TrecResultProperty> goldStandardProperties = new ArrayList<TrecResultProperty>();
		for(String line : new InputFile(new File(goldStandard)).lines()){
			TrecResultProperty e = new TrecResultProperty(line);
			if(id == e.groupId()) {
				goldStandardProperties.add(e);
			}
		}
		Collections.sort(goldStandardProperties);
		return goldStandardProperties.subList(0, Math.min(topK, goldStandardProperties.size()));
	}
	
	private static List<TrecGoldStandardProperty> getGoldStandardProperties(String goldStandard, int topK, int id) throws Exception {
		List<TrecGoldStandardProperty> goldStandardProperties = new ArrayList<TrecGoldStandardProperty>();
		for(String line : new InputFile(new File(goldStandard)).lines()){
			TrecGoldStandardProperty e = new TrecGoldStandardProperty(line);
			if(id == e.groupId() && e.rank() > 0) {
				goldStandardProperties.add(e);
			}
		}
		Collections.sort(goldStandardProperties);
		return goldStandardProperties.subList(0, Math.min(topK, goldStandardProperties.size()));
	}
	
	private static int id(String line) {
		return Integer.parseInt(split(line)[1]);
	}

	private static double measureResult(String line) {
		return Double.parseDouble(split(line)[2]);
	}

	private static String[] split(String line) {
		return StringUtils.split(line, "\t");
	}

	private static String goldStandardQRels(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "dbpedia-enhanced.qrels");
		qrels.put("dbpedia-ontology", "dbpedia-enhanced-ontology.qrels");
		qrels.put("dbpedia-with-labels", "dbpedia-enhanced-with-labels.qrels");
		qrels.put("yago1", "yago1-enhanced.qrels");
		qrels.put("yago1-simple", "yago1-simple.qrels");
		return "../evaluation/gold-standards/" + qrels.get(knowledgeBase);
	}
	
	private static String resultDirectory(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "dbpedia-results/");
		qrels.put("dbpedia-ontology", "dbpedia-ontology-results/");
		qrels.put("dbpedia-with-labels", "dbpedia-with-labels-results/");
		qrels.put("yago1", "yago1-results/");
		qrels.put("yago1-simple", "yago1-simple-results/");
		return "../evaluation/results/" + qrels.get(knowledgeBase);
	}
}

class TrecGoldStandardProperty implements Comparable<TrecGoldStandardProperty>{

	private String line;

	public TrecGoldStandardProperty(String line) {
		this.line = line;
	}
	
	public String value(){
		return StringUtils.split(line, " ")[2];
	}
	
	public Double rank(){
		return Double.parseDouble(StringUtils.split(line, " ")[3]);
	}
	
	public int groupId(){
		return Integer.parseInt(StringUtils.split(line, " ")[0]);
	}
	
	@Override
	public String toString() {
		return value() + " (" + rank() + ")";
	}
	
	@Override
	public int compareTo(TrecGoldStandardProperty other) {
		return (int)Math.signum(other.rank() - this.rank());
	}
}
