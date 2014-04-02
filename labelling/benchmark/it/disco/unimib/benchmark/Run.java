package it.disco.unimib.benchmark;

import it.disco.unimib.baseline.MajorityVote;
import it.disco.unimib.baseline.RankedMajority;
import it.disco.unimib.baseline.TRankNER;
import it.disco.unimib.baseline.TRankTypeRank;
import it.disco.unimib.labelling.HttpConnector;
import it.disco.unimib.labelling.Tagme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Run {

	public static void main(String[] args) throws Exception {
		for(BenchmarkConfiguration configuration : configurations()){
			Metric[] metrics = metrics();
			new Benchmark(configuration.annotator(), configuration.ranker()).on(goldStandard(), metrics);
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

	private static FileSystemConnector[] goldStandard() {
		List<FileSystemConnector> connectors = new ArrayList<FileSystemConnector>();
		for(File file : new File("../evaluation/gold-standard").listFiles()){
			connectors.add(new FileSystemConnector(file));
		}
		return connectors.toArray(new FileSystemConnector[connectors.size()]);
	}
}
