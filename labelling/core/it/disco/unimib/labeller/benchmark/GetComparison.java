package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class GetComparison {

	public static void main(String[] args) throws Exception {
		
		Command command = new Command().withArgument("kb", "the knowledge base for which results are analyzed, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
					 .withArgument("alg", "the algorithm whose results are analyzed")
					 .withArgument("k", "restrict the analisys to the top k results")
					 .withArgument("t", "the MAP threshold under which a result is considered to be improvable");
		try{
			command.parse(args);
		}catch(Exception e){
			System.err.println(command.explainArguments());
			return;
		}
		
		int topK = Integer.parseInt(command.argumentAsString("k"));
		double threshold = Double.parseDouble(command.argumentAsString("t"));
		String goldStandard = goldStandardQRels(command.argumentAsString("kb"));
		String qrels = resultDirectory(command.argumentAsString("kb")) + command.argumentAsString("alg");
		GoldStandard goldStandardGroups = new BenchmarkParameters(args).goldStandard();
		
		List<String> results = executeCommand("trec_eval -q -M " + topK + " -m "
											+ "map"
											+ " " + goldStandard
											+ " " + qrels);
		
		System.out.println("results for " + qrels + " on " + goldStandard);
		System.out.println("overall: " + measure(results.get(results.size() - 1)));
		
		List<String> notPerfectResults = new ArrayList<String>();
		List<String> particularResults = results.subList(0, results.size() - 1);
		for (String line : particularResults) {
			if(measure(line) < threshold) {
				notPerfectResults.add(line);
			}
		}
		double incorrect = (double)notPerfectResults.size();
		double total = (double)particularResults.size();
		double incorrectRatio = incorrect / total;
		System.out.println(incorrect + " improvable groups over " + total + " (" + incorrectRatio + ")");
		
		for(String result : notPerfectResults){
			int id = id(result);
			System.out.println("------------------------------------------");
			GoldStandardGroup groupById = goldStandardGroups.getGroupById(id);
			System.out.println(id + " " + measure(result) + " " + groupById.context() + " (" + groupById.elements().size() + " elements)");
			System.out.println(groupById.elements().subList(0, Math.min(5, groupById.elements().size())) + " ... ");
			System.out.println("EXPECTED PREDICATES (rel. judgement)\tACTUAL PREDICATES (score)");
			
			List<TrecGoldStandardPredicate> goldStandardPredicates = getGoldStandardPredicates(goldStandard, topK, id);
			List<TrecResultPredicate> resultingPredicates = getResultingPredicates(qrels, topK, id);
			
			for(int i=0; i < Math.max(goldStandardPredicates.size(), resultingPredicates.size()); i++){
				String line = "";
				if(i < goldStandardPredicates.size()) line+=goldStandardPredicates.get(i);
				line+="\t";
				if(i < resultingPredicates.size()) line+=resultingPredicates.get(i);
				System.out.println(line);
			}
		}
	}

	private static List<TrecResultPredicate> getResultingPredicates(String goldStandard, int topK, int id) throws Exception {
		List<TrecResultPredicate> goldStandardPredicates = new ArrayList<TrecResultPredicate>();
		for(String line : new InputFile(new File(goldStandard)).lines()){
			TrecResultPredicate e = new TrecResultPredicate(line);
			if(id == e.groupId()) {
				goldStandardPredicates.add(e);
			}
		}
		Collections.sort(goldStandardPredicates);
		return goldStandardPredicates.subList(0, Math.min(topK, goldStandardPredicates.size()));
	}
	
	private static List<TrecGoldStandardPredicate> getGoldStandardPredicates(String goldStandard, int topK, int id) throws Exception {
		List<TrecGoldStandardPredicate> goldStandardPredicates = new ArrayList<TrecGoldStandardPredicate>();
		for(String line : new InputFile(new File(goldStandard)).lines()){
			TrecGoldStandardPredicate e = new TrecGoldStandardPredicate(line);
			if(id == e.groupId() && e.rank() > 0) {
				goldStandardPredicates.add(e);
			}
		}
		Collections.sort(goldStandardPredicates);
		return goldStandardPredicates.subList(0, Math.min(topK, goldStandardPredicates.size()));
	}
	
	private static int id(String line) {
		return Integer.parseInt(split(line)[1]);
	}

	private static double measure(String line) {
		return Double.parseDouble(split(line)[2]);
	}

	private static String[] split(String line) {
		return StringUtils.split(line, "\t");
	}

	private static List<String> executeCommand(String command) throws Exception {
		Process result = Runtime.getRuntime().exec(command);
		result.waitFor();
		return IOUtils.readLines(result.getInputStream());
	}
	
	private static String goldStandardQRels(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "gold-standard.qrels");
		qrels.put("dbpedia-with-labels", "gold-standard-with-labels.qrels");
		qrels.put("yago1", "gold-standard-sarawagi.qrels");
		qrels.put("yago1-simple", "gold-standard-sarawagi-simple.qrels");
		return "../evaluation/results/" + qrels.get(knowledgeBase);
	}
	
	private static String resultDirectory(String knowledgeBase){
		HashMap<String, String> qrels = new HashMap<String, String>();
		qrels.put("dbpedia", "dbpedia-results/");
		qrels.put("dbpedia-with-labels", "dbpedia-with-labels-results/");
		qrels.put("yago1", "yago1-results/");
		qrels.put("yago1-simple", "yago1-simple-results/");
		return "../evaluation/results/" + qrels.get(knowledgeBase);
	}
}

class TrecResultPredicate implements Comparable<TrecResultPredicate>{

	private String line;

	public TrecResultPredicate(String line) {
		this.line = line;
	}
	
	public String value(){
		return StringUtils.split(line, " ")[2];
	}
	
	public Double rank(){
		return Double.parseDouble(StringUtils.split(line, " ")[4]);
	}
	
	public int groupId(){
		return Integer.parseInt(StringUtils.split(line, " ")[0]);
	}
	
	@Override
	public String toString() {
		return value() + " (" + rank() + ")";
	}
	
	@Override
	public int compareTo(TrecResultPredicate other) {
		return (int)Math.signum(other.rank() - this.rank());
	}
}

class TrecGoldStandardPredicate implements Comparable<TrecGoldStandardPredicate>{

	private String line;

	public TrecGoldStandardPredicate(String line) {
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
	public int compareTo(TrecGoldStandardPredicate other) {
		return (int)Math.signum(other.rank() - this.rank());
	}
}
