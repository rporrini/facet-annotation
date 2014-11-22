package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;

import java.util.List;

public class Benchmark {

	private AnnotationAlgorithm algorithm;

	public Benchmark(AnnotationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void on(GoldStandardFacet[] groups, Summary summary) throws Exception {
		for(GoldStandardFacet group : groups){
			new Events().debug("processing gold standard " + group.context() + " " + group.label());
			List<String> elements = group.elements();
			List<CandidatePredicate> labels = algorithm.typeOf(group.context(), elements);
			summary.track(group, labels);
		}
	}
}