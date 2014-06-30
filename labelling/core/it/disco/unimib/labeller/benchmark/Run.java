package it.disco.unimib.labeller.benchmark;

public class Run {

	public static void main(String[] args) throws Exception {
		BenchmarkParameters parameters = new BenchmarkParameters(args);
		Summary summary = parameters.analysis();
		BenchmarkConfiguration configuration = parameters.configuration();
		new Benchmark(configuration.getAlgorithm()).on(parameters.goldStandard(), summary);
		System.out.println();
		System.out.println(summary.result());
	}
}
