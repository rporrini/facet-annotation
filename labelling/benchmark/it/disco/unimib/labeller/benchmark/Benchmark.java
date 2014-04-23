package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.Events;

import java.util.List;

public class Benchmark {

	private AnnotationAlgorithm algorithm;

	public Benchmark(AnnotationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void on(GoldStandardGroup[] groups, Metric[] metrics) throws Exception {
		for(GoldStandardGroup group : groups){
			new Events().debug("processing gold standard " + group.context() + " " + group.label());
			List<String> elements = group.elements();
			List<AnnotationResult> labels = algorithm.typeOf(group.context(), elements);
			for(Metric metric : metrics){
				metric.track(group, labels);
			}
		}
	}
}