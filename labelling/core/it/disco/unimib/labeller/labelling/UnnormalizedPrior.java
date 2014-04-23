package it.disco.unimib.labeller.labelling;

public class UnnormalizedPrior {

	private Distribution distribution;

	public UnnormalizedPrior(Distribution valueDistribution) {
		this.distribution = valueDistribution;
	}

	public double of(String predicate) {
		double totalScore = 0.0;
		for(String value : this.distribution.values()){
			totalScore += distribution.scoreOf(predicate, value);
		}
		return totalScore;
	}
}
