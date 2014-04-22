package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.baseline.MajorityVote;
import it.disco.unimib.labeller.baseline.RankedMajority;
import it.disco.unimib.labeller.baseline.TRankNER;
import it.disco.unimib.labeller.baseline.TRankTypeRank;
import it.disco.unimib.labeller.labelling.HttpConnector;
import it.disco.unimib.labeller.labelling.Tagme;

import java.io.File;

public class Run {

	public static void main(String[] args) throws Exception {
		for(BenchmarkConfiguration configuration : configurations()){
			Metric[] metrics = metrics();
			new Benchmark(configuration.typeAnnotation()).on(goldStandard(), metrics);
			for(Metric metric : metrics){
				System.out.println();
				System.out.println(configuration.name());
				System.out.println(metric.result());
			}
		}
	}

	private static BenchmarkConfiguration[] configurations() {
		return new BenchmarkConfiguration[]{
			new BenchmarkConfiguration("trank whole pipeline + majority vote")
										.withAnnotator(new TRankNER())
										.withRanker(new TRankTypeRank(new MajorityVote())),
			new BenchmarkConfiguration("trank whole pipeline + ranked majority")
										.withAnnotator(new TRankNER())
										.withRanker(new TRankTypeRank(new RankedMajority())),
			new BenchmarkConfiguration("tagme ner + trank type ranking + majority vote")
										.withAnnotator(new Tagme(new HttpConnector()))
										.withRanker(new TRankTypeRank(new MajorityVote())),
			new BenchmarkConfiguration("tagme ner + trank type ranking + ranked majority")
										.withAnnotator(new Tagme(new HttpConnector()))
										.withRanker(new TRankTypeRank(new RankedMajority()))
		};
	}

	private static Metric[] metrics() {
		return new Metric[]{new Qualitative()};
	}

	private static GoldStandardGroup[] goldStandard() {
		return new OrderedGroups(new UnorderedGroups(new File("../evaluation/gold-standard"))).getGroups();
	}
}
