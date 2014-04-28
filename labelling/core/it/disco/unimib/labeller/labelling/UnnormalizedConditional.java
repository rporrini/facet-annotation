package it.disco.unimib.labeller.labelling;

public class UnnormalizedConditional {

	private Distribution distribution;
	private NormalizedPrior prior;
	private double kp;

	public UnnormalizedConditional(Distribution distribution, NormalizedPrior prior) {
		this.distribution = distribution;
		this.prior = prior;
		this.kp = 0.01;
	}

	public double of(String predicate, String value) {
		double score = Math.log(distribution.scoreOf(predicate, value) + 1);
		double prior = this.prior.of(predicate);
		double all = 0.0;
		for(String otherPredicate : distribution.predicates()){
			all += Math.log(distribution.scoreOf(otherPredicate, value) + 1);
		}
		return ((kp * prior) + score )/(kp + all);
	}
}
