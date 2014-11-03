package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ResultSetCardinalityVsPerformance {
	
	public static void main(String[] args) throws Exception {
		Command command = new Command()
								.withArgument("m", "the considered metric, from trec_eval")
								.withArgument("alg", "the path of the results of the first algorithm")
								.withArgument("k", "restricts the analysis to the top k results")
								.withArgument("qrels", "path of the gold standard qrels")
								.parse(args);
		String algorithm = command.argumentAsString("alg");
		String metric = command.argumentAsString("m");
		String qrels = command.argumentAsString("qrels");
		int k = Integer.parseInt(command.argumentAsString("k"));
		
		HashMap<Integer, Double> results = get(TrecEval.onlySingle(k, qrels, algorithm, metric));
		HashMap<Integer, Double> cardinalities = get(TrecEval.onlySingle(100000, qrels, algorithm, "num_ret"));
		
		for(int id : results.keySet()){
			System.out.println(cardinalities.get(id) + "\t" + results.get(id));
		}
	}

	private static HashMap<Integer, Double> get(List<String> performance) {
		HashMap<Integer,Double> results = new HashMap<Integer, Double>();
		for(String result : performance){
			String[] splittedLine = StringUtils.split(result, "\t");
			results.put(Integer.parseInt(splittedLine[1]), Double.parseDouble(splittedLine[2]));
		}
		return results;
	}
}
