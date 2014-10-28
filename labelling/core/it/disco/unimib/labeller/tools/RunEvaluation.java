package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.Summary;


public class RunEvaluation {

	public static void main(String[] args) throws Exception {
		Command command = new Command().withArgument("kb", "the knowledge base to use, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
					 .withArgument("algorithm", "the algorithm to use, namely mh, mhw, mhsw, mhwv, mhwcv, ml")
					 .withArgument("occurrences", "the function applied to count each occurrence, namely simple, contextualized")
					 .withArgument("summary", "the format of the results, namely questionnaire, trec")
					 .parse(args);
		
		BenchmarkParameters parameters = new BenchmarkParameters(command);
		Summary summary = parameters.analysis();
		new Benchmark(parameters.algorithm()).on(parameters.goldStandard().getGroups(), summary);
		System.out.println(summary.result());
	}
}
