package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.properties.AnnotationAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Benchmark {

	private AnnotationAlgorithm algorithm;

	public Benchmark(AnnotationAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void on(GoldStandardFacet[] groups, Summary summary) throws Exception {
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		List<Future<BenchmarkResult>> results = new ArrayList<Future<BenchmarkResult>>();
		for(final GoldStandardFacet group : groups){
			Future<BenchmarkResult> future = executor.submit(new Callable<BenchmarkResult>() {
				@Override
				public BenchmarkResult call() throws Exception {
					new Events().debug("processing gold standard " + group.context() + " " + group.label());
					String[] elements = group.elements().toArray(new String[group.elements().size()]);
					return new BenchmarkResult(group, algorithm.typeOf(new ContextualizedValues(group.context(), elements)));
				}
			});
			results.add(future);
		}
		
		executor.shutdown();
		while(!executor.isTerminated()){}
		
		for(Future<BenchmarkResult> future : results){
			BenchmarkResult result = future.get();
			summary.track(result.facet, result.results);
		}
	}
}

class BenchmarkResult{
	
	public GoldStandardFacet facet;
	public List<CandidateResource> results;

	public BenchmarkResult(GoldStandardFacet facet, List<CandidateResource> results){
		this.facet = facet;
		this.results = results;
	}
}