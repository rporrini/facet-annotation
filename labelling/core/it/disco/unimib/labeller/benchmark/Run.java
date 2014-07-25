package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

public class Run {

	public static void main(String[] args) throws Exception {
		BenchmarkParameters parameters = new BenchmarkParameters(args);
		Summary summary = parameters.analysis();
		AnnotationAlgorithm configuration = parameters.configuration();
		new Benchmark(configuration).on(parameters.goldStandard(), summary);
		System.out.println();
		System.out.println(summary.result());
	}
}
