package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.util.List;

public class Benchmark {

	private AnnotationAlgorithm algorithm;

	public Benchmark(AnnotationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void on(GoldStandardGroup[] groups, Metric[] metrics) throws Exception {
		for(GoldStandardGroup group : groups){
			List<String> elements = group.elements();
			AnnotationResult actualType = typeOf(elements);
			for(Metric metric : metrics){
				metric.track(group.domain(), group.context(), group.label(), actualType.value());
			}
		}
	}

	public AnnotationResult typeOf(List<String> elements) throws Exception {
		return algorithm.typeOf(elements);
	}

}