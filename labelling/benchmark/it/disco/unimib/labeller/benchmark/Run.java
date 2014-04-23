package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class Run {

	public static void main(String[] args) throws Exception {
		for(BenchmarkConfiguration configuration : configurations()){
			Summary[] summaries = summaries();
			new Benchmark(configuration.getAlgorithm()).on(goldStandard(), summaries);
			for(Summary metric : summaries){
				System.out.println();
				System.out.println(configuration.name());
				System.out.println(metric.result());
			}
		}
	}

	private static BenchmarkConfiguration[] configurations() throws Exception {
		return new BenchmarkConfiguration[]{
			new BenchmarkConfiguration("majority").majorityAnnotation(0.05)
		};
	}

	private static Summary[] summaries() {
		return new Summary[]{new TrecEval("majority")};
	}

	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard"))).getGroups();
	}
}
