package it.disco.unimib.labeller.predicates;

public class UnnormalizedPrior {

	private Distribution distribution;

	public UnnormalizedPrior(Distribution valueDistribution) {
		this.distribution = valueDistribution;
	}

	public double of(String predicate) {
		double totalScore = 0.0;
		for(String value : this.distribution.values()){
			totalScore += Math.log(distribution.scoreOf(predicate, value) + 1.0d + 0.0001d);
		}
		return totalScore;
	}
}
