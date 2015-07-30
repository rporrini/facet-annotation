package it.disco.unimib.labeller.properties;

public class UnnormalizedConditional {

	private PropertyDistribution distribution;
	private NormalizedPrior prior;
	private double kp;

	public UnnormalizedConditional(PropertyDistribution distribution, NormalizedPrior prior) {
		this.distribution = distribution;
		this.prior = prior;
		this.kp = 0.01;
	}

	public double of(String property, String value) {
		double score = Math.log(distribution.scoreOf(property, value) + 1.0d);
		double prior = this.prior.of(property);
		double all = 0.0;
		for(String otherProperty : distribution.properties()){
			all += Math.log(distribution.scoreOf(otherProperty, value) + 1.0d);
		}
		return ((kp * prior) + score )/(kp + all);
	}
}
