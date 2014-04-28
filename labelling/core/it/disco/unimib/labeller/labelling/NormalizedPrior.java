package it.disco.unimib.labeller.labelling;

import java.util.ArrayList;
import java.util.List;

public class NormalizedPrior {

	private Normalize normalizer;
	private UnnormalizedPrior unnormalizedPrior;

	public NormalizedPrior(Distribution distribution, UnnormalizedPrior prior) {
		this.unnormalizedPrior = prior;
		this.normalizer = normalized(distribution);
	}

	private Normalize normalized(Distribution distribution) {
		List<Double> scores = new ArrayList<Double>();
		for(String predicate : distribution.predicates()){
			scores.add(unnormalizedPrior.of(predicate));
		}
		return new Normalize(scores.toArray(new Double[scores.size()]));
	}

	public double of(String predicate) {
		return normalizer.value(unnormalizedPrior.of(predicate));
	}

}
