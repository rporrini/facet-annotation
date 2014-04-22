package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Annotator;
import it.disco.unimib.labeller.labelling.TypeRanker;

import java.util.List;

public class Benchmark {

	private Annotator annotator;
	private TypeRanker ranker;

	public Benchmark(Annotator annotator, TypeRanker ranker) {
		this.annotator = annotator;
		this.ranker = ranker;
	}

	public void on(GoldStandardGroup[] groups, Metric[] metrics) throws Exception {
		for(GoldStandardGroup group : groups){
			List<String> elements = group.elements();
			List<String> entities = annotator.annotate(elements.toArray(new String[elements.size()]));
			AnnotationResult actualType = ranker.typeOf(entities.toArray(new String[entities.size()]));
			for(Metric metric : metrics){
				metric.track(group.domain(), group.context(), group.label(), actualType.value());
			}
		}
	}

}
