package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;


public class RunEvaluation {

	public static void main(String[] args) throws Exception {
		BenchmarkParameters parameters = benchmarkParameters(args);
		AnnotationAlgorithm algorithm = parameters.algorithm();
		GoldStandard goldStandard = parameters.goldStandard();
		
		Summary summary = parameters.analysis();
		runBenchmark(summary, algorithm, goldStandard);
		System.out.println(summary.result());
	}

	public static void runBenchmark(Summary summary, AnnotationAlgorithm algorithm, GoldStandard goldStandard) throws Exception {
		new Benchmark(algorithm).on(goldStandard.getFacets(), summary);
	}

	public static BenchmarkParameters benchmarkParameters(String[] args) throws Exception {
		Command command = new Command().withArgument("kb", "the knowledge base to use, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple")
					 .withArgument("algorithm", "the algorithm to use, namely mh, mhw, ml")
					 .withArgument("occurrences", "the function applied to count each occurrence, namely simple, contextualized")
					 .withArgument("summary", "the format of the results, namely questionnaire, trec")
					 .withArgument("context", "context matching, namely no, partial, complete")
					 .parse(args);
		
		return new BenchmarkParameters(command);
	}
}
