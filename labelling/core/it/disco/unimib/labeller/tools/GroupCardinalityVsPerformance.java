package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class GroupCardinalityVsPerformance {

	public static void main(String[] args) throws Exception {
		Command command = new Command()
								.withArgument("m", "the considered metric, from trec_eval")
								.withArgument("alg", "the path of the results of the first algorithm")
								.withArgument("k", "restricts the analysis to the top k results")
								.withArgument("qrels", "path of the gold standard qrels")
								.withArgument("gs", "path of the gold standard")
								.parse(args);
		String algorithm1 = command.argumentAsString("alg");
		String metric = command.argumentAsString("m");
		String qrels = command.argumentAsString("qrels");
		String goldStandard = command.argumentAsString("gs");
		int k = Integer.parseInt(command.argumentAsString("k"));
		
		UnorderedFacets groups = new UnorderedFacets(new File(goldStandard));
		
		for(String result : TrecEval.onlySingle(k, qrels, algorithm1, metric)){
			String[] splittedLine = StringUtils.split(result, "\t");
			int cardinality = groups.getGroupById(Integer.parseInt(splittedLine[1])).elements().size();
			double score = Double.parseDouble(splittedLine[2]);
			
			System.out.println(cardinality + "\t" + score);
		}
	}
}
