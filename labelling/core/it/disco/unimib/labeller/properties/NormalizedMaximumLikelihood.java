package it.disco.unimib.labeller.properties;

import java.util.ArrayList;
import java.util.List;

public class NormalizedMaximumLikelihood {

	private MaximumLikelihood likelihood;
	private Normalize normalize;

	public NormalizedMaximumLikelihood(PropertyDistribution distribution, NormalizedConditional conditional, NormalizedPrior prior){
		this.likelihood = new MaximumLikelihood(distribution, conditional, prior);
		this.normalize = normalize(distribution);
	}

	private Normalize normalize(PropertyDistribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String property : distribution.properties()){
			scores.add(likelihood.of(property));
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}
	
	public double of(String property){
		return normalize.value(likelihood.of(property));
	}
}
