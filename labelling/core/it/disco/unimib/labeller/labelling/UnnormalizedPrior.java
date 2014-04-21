package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UnnormalizedPrior {

	private Distribution distribution;
	private Set<String> values;

	public UnnormalizedPrior(HashMap<String, List<SearchResult>> valueDistribution) {
		this.distribution = new Distribution(valueDistribution);
		this.values = valueDistribution.keySet();
	}

	public double of(String predicate) {
		double totalScore = 0.0;
		for(String value : this.values){
			totalScore += Math.log(distribution.scoreOf(predicate, value) + 1 + 0.001);
		}
		return totalScore;
	}
}
