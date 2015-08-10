package it.disco.unimib.labeller.properties;

import java.util.ArrayList;
import java.util.List;

public class NormalizedPrior {

	private Normalize normalizer;
	private UnnormalizedPrior unnormalizedPrior;

	public NormalizedPrior(PropertyDistribution distribution, UnnormalizedPrior prior) {
		this.unnormalizedPrior = prior;
		this.normalizer = normalized(distribution);
	}

	private Normalize normalized(PropertyDistribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String property : distribution.properties()){
			scores.add(unnormalizedPrior.of(property));
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String property) {
		return normalizer.value(unnormalizedPrior.of(property));
	}

}
