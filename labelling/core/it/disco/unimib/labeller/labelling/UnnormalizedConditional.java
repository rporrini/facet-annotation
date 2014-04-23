package it.disco.unimib.labeller.labelling;

public class UnnormalizedConditional {

	private Distribution distribution;
	private NormalizedPrior prior;

	public UnnormalizedConditional(Distribution distribution) {
		this.distribution = distribution;
		this.prior = new NormalizedPrior(distribution);
	}

	public double of(String predicate, String value) {
		double score = distribution.scoreOf(predicate, value);
		double prior = this.prior.of(predicate);
		double all = 0.0;
		for(String otherPredicate : distribution.predicates()){
			all += distribution.scoreOf(otherPredicate, value);
		}
		return ((prior) + score )/(all);
	}
}
