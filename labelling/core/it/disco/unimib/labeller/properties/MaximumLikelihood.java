package it.disco.unimib.labeller.properties;

public class MaximumLikelihood {

	private PropertyDistribution distribution;
	private NormalizedConditional conditional;
	private NormalizedPrior prior;

	public MaximumLikelihood(PropertyDistribution distribution, NormalizedConditional conditional, NormalizedPrior prior) {
		this.distribution = distribution;
		this.conditional = conditional;
		this.prior = prior;
	}

	public double of(String property) {
		double sum = 1.0;
		for(String value : distribution.values()){
			sum *= conditional.of(property, value);
		}
		return sum / prior.of(property);
	}
}
