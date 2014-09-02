package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class GetComparison {

	public static void main(String[] args) throws Exception {
		String goldStandard = "../evaluation/results/gold-standard-sarawagi-simple.qrels";
		String qrels = "../evaluation/results/yago1-simple-results/mh-contextualized-complete.qrels";
		String goldStandardGroups = "../evaluation/gold-standard-sarawagi-simple";
		
		List<String> results = executeCommand("trec_eval -q -M 10 -m "
											+ "map"
											+ " " + goldStandard
											+ " " + qrels);
		
		System.out.println("results for " + qrels + " on " + goldStandard);
		System.out.println("overall: " + measure(results.get(results.size() - 1)));
		
		List<String> notPerfectResults = new ArrayList<String>();
		List<String> particularResults = results.subList(0, results.size() - 1);
		for (String line : particularResults) {
			if(measure(line) < 0.8) {
				notPerfectResults.add(line);
			}
		}
		double incorrect = (double)notPerfectResults.size();
		double total = (double)particularResults.size();
		double incorrectRatio = incorrect / total;
		System.out.println("there where " + incorrect + " improvable groups over " + total + " (" + incorrectRatio + ")");
		
		InputFile resultingPredicates = new InputFile(new File(qrels));
		HashMap<String, List<String>> qRelsForIds = new HashMap<String, List<String>>();
		for(String line : resultingPredicates.lines()){
			String id = StringUtils.split(line, " ")[0];
			String predicate = StringUtils.split(line, " ")[2];
			if(!qRelsForIds.containsKey(id)) qRelsForIds.put(id, new ArrayList<String>());
			qRelsForIds.get(id).add(predicate);
		}
		
		UnorderedGroups groups = new UnorderedGroups(new File(goldStandardGroups));
		for(String result : notPerfectResults){
			int id = id(result);
			System.out.println("------------------------------------------");
			GoldStandardGroup groupById = groups.getGroupById(id);
			System.out.println(measure(result) + " " + groupById.context() + " (" + groupById.elements().size() + " elements)");
		}
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
}
