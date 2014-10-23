package it.disco.unimib.labeller.predicates;

import java.util.ArrayList;
import java.util.List;

public class NormalizedConditional {

	private Normalize normalizer;
	private UnnormalizedConditional conditional;
	
	public NormalizedConditional(Distribution distribution, NormalizedPrior prior, UnnormalizedConditional unnormalizedConditional) {
		this.conditional = unnormalizedConditional;
		this.normalizer = normalize(distribution);
	}

	private Normalize normalize(Distribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String predicate : distribution.predicates()){
			for(String value : distribution.values()){
				scores.add(conditional.of(predicate, value));
			}
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String predicate, String value) {
		return normalizer.value(conditional.of(predicate, value));
	}
}
