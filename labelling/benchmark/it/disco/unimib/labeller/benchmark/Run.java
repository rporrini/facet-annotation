package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class Run {

	public static void main(String[] args) throws Exception {
		for(BenchmarkConfiguration configuration : configurations()){
			Summary[] metrics = metrics();
			new Benchmark(configuration.getAlgorithm()).on(goldStandard(), metrics);
			for(Summary metric : metrics){
				System.out.println();
				System.out.println(configuration.name());
				System.out.println(metric.result());
			}
		}
	}

	private static BenchmarkConfiguration[] configurations() throws Exception {
		return new BenchmarkConfiguration[]{
			new BenchmarkConfiguration("maximum likelihood").predicateAnnotation(),
			//new BenchmarkConfiguration("majority").majorityAnnotation(0.1)
		};
	}

	private static Summary[] metrics() {
		return new Summary[]{new Questionnaire()};
	}

	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard"))).getGroups();
	}
}
