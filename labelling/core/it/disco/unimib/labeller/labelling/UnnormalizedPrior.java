package it.disco.unimib.labeller.labelling;

public class UnnormalizedPrior {

	private Distribution distribution;

	public UnnormalizedPrior(Distribution valueDistribution) {
		this.distribution = valueDistribution;
	}

	public double of(String predicate) {
		double totalScore = 0.0;
		for(String value : this.distribution.values()){
			totalScore += Math.log(distribution.scoreOf(predicate, value) + 1 + 0.001);
		}
		return totalScore;
	}
}
