package it.disco.unimib.labeller.labelling;

public class MaximumLikelihood {

	private Distribution distribution;
	private NormalizedConditional conditional;
	private NormalizedPrior prior;

	public MaximumLikelihood(Distribution distribution) {
		this.distribution = distribution;
		this.conditional = new NormalizedConditional(distribution);
		this.prior = new NormalizedPrior(distribution);
	}

	public double of(String predicate) {
		double sum = 1.0;
		for(String value : distribution.values()){
			sum *= conditional.of(predicate, value);
		}
		return sum / prior.of(predicate);
	}
}
