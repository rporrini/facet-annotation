package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.FileSystemConnector;
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

	public void on(FileSystemConnector[] connectors, Metric[] metrics) throws Exception {
		for(FileSystemConnector connector : connectors){
			GoldStandardGroup group = new GoldStandardGroup(connector);
			List<String> elements = group.elements();
			List<String> entities = annotator.annotate(elements.toArray(new String[elements.size()]));
			String actualType = ranker.typeOf(entities.toArray(new String[entities.size()]));
			for(Metric metric : metrics){
				metric.track(group.context(), group.label(), actualType);
			}
		}
	}

}
