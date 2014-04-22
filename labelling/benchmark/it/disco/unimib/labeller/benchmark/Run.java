package it.disco.unimib.labeller.benchmark;

import java.io.File;

public class Run {

	public static void main(String[] args) throws Exception {
		for(BenchmarkConfiguration configuration : configurations()){
			Metric[] metrics = metrics();
			new Benchmark(configuration.getAlgorithm()).on(goldStandard(), metrics);
			for(Metric metric : metrics){
				System.out.println();
				System.out.println(configuration.name());
				System.out.println(metric.result());
			}
		}
	}

	private static BenchmarkConfiguration[] configurations() throws Exception {
		return new BenchmarkConfiguration[]{
//			new BenchmarkConfiguration("trank whole pipeline + majority vote")
//										.typeAnnotation(new TRankNER(), new TRankTypeRank(new MajorityVote())),
//			new BenchmarkConfiguration("trank whole pipeline + ranked majority")
//										.typeAnnotation(new TRankNER(), new TRankTypeRank(new RankedMajority())),
//			new BenchmarkConfiguration("tagme ner + trank type ranking + majority vote")
//										.typeAnnotation(new Tagme(new HttpConnector()), new TRankTypeRank(new MajorityVote())),
//			new BenchmarkConfiguration("tagme ner + trank type ranking + ranked majority")
//										.typeAnnotation(new Tagme(new HttpConnector()), new TRankTypeRank(new RankedMajority())),
//			new BenchmarkConfiguration("maximum likelihood")
//										.predicateAnnotation()
			new BenchmarkConfiguration("majority").majorityAnnotation(0.1)
		};
	}

	private static Metric[] metrics() {
		return new Metric[]{new Qualitative()};
	}

	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard"))).getGroups();
	}
}
