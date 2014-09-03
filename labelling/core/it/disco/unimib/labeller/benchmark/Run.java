package it.disco.unimib.labeller.benchmark;


public class Run {

	public static void main(String[] args) throws Exception {
		BenchmarkParameters parameters = new BenchmarkParameters(args);
		Summary summary = parameters.analysis();
		new Benchmark(parameters.algorithm()).on(parameters.goldStandard().getGroups(), summary);
		System.out.println(summary.result());
	}
}
