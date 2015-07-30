package it.disco.unimib.labeller.properties;

import java.util.ArrayList;
import java.util.List;

public class NormalizedConditional {

	private Normalize normalizer;
	private UnnormalizedConditional conditional;
	
	public NormalizedConditional(PropertyDistribution distribution, NormalizedPrior prior, UnnormalizedConditional unnormalizedConditional) {
		this.conditional = unnormalizedConditional;
		this.normalizer = normalize(distribution);
	}

	private Normalize normalize(PropertyDistribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String property : distribution.properties()){
			for(String value : distribution.values()){
				scores.add(conditional.of(property, value));
			}
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String property, String value) {
		return normalizer.value(conditional.of(property, value));
	}
}
