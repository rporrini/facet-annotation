package it.disco.unimib.labeller.predicates;

public class MaximumLikelihood {

	private Distribution distribution;
	private NormalizedConditional conditional;
	private NormalizedPrior prior;

	public MaximumLikelihood(Distribution distribution, NormalizedConditional conditional, NormalizedPrior prior) {
		this.distribution = distribution;
		this.conditional = conditional;
		this.prior = prior;
	}

	public double of(String predicate) {
		double sum = 1.0;
		for(String value : distribution.values()){
			sum *= conditional.of(predicate, value);
		}
		return sum / prior.of(predicate);
	}
}
