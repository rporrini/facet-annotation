package it.disco.unimib.labeller.predicates;

import java.util.ArrayList;
import java.util.List;

public class NormalizedMaximumLikelihood {

	private MaximumLikelihood likelihood;
	private Normalize normalize;

	public NormalizedMaximumLikelihood(Distribution distribution, NormalizedConditional conditional, NormalizedPrior prior){
		this.likelihood = new MaximumLikelihood(distribution, conditional, prior);
		this.normalize = normalize(distribution);
	}

	private Normalize normalize(Distribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String predicate : distribution.predicates()){
			scores.add(likelihood.of(predicate));
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}
	
	public double of(String predicate){
		return normalize.value(likelihood.of(predicate));
	}
}