package it.disco.unimib.labeller.labelling;

import java.util.ArrayList;
import java.util.List;

public class NormalizedPrior {

	private Distribution distribution;
	private Normalize normalizer;

	public NormalizedPrior(Distribution distribution) {
		this.distribution = distribution;
		this.normalizer = normalized(distribution);
	}

	private Normalize normalized(Distribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(distribution);
		for(String predicate : distribution.predicates()){
			scores.add(unnormalizedPrior.of(predicate));
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String predicate) {
		return normalizer.value(new UnnormalizedPrior(distribution).of(predicate));
	}

}
