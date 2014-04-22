package it.disco.unimib.labeller.labelling;

import java.util.ArrayList;
import java.util.List;

public class NormalizedMaximumLikelihood {

	private MaximumLikelihood likelihood;
	private Normalize normalize;

	public NormalizedMaximumLikelihood(Distribution distribution){
		this.likelihood = new MaximumLikelihood(distribution);
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
