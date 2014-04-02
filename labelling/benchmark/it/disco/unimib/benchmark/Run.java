package it.disco.unimib.benchmark;

import it.disco.unimib.baseline.TRankBaseline;
import it.disco.unimib.labelling.HttpConnector;
import it.disco.unimib.labelling.Tagme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Run {

	public static void main(String[] args) throws Exception {
		Metric[] metrics = metrics();
		
		new Benchmark(new Tagme(new HttpConnector()), new TRankBaseline()).on(goldStandard(), metrics);
		
		for(Metric metric : metrics){
			System.out.println(metric.result());
		}
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
