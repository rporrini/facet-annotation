package it.disco.unimib.labeller.labelling;

import java.util.ArrayList;
import java.util.List;

public class NormalizedConditional {

	private Distribution distribution;
	private Normalize normalizer;
	
	public NormalizedConditional(Distribution distribution) {
		this.distribution = distribution;
		this.normalizer = normalize(distribution);
	}

	private Normalize normalize(Distribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		UnnormalizedConditional conditional = new UnnormalizedConditional(distribution);
		for(String predicate : distribution.predicates()){
			for(String value : distribution.values()){
				scores.add(conditional.of(predicate, value));
			}
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String predicate, String value) {
		return normalizer.value(new UnnormalizedConditional(distribution).of(predicate, value));
	}
}
