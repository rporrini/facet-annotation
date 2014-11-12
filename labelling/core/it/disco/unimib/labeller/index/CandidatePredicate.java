package it.disco.unimib.labeller.index;



public class CandidatePredicate implements Comparable<CandidatePredicate>{

	private double[] scores;
	private RDFPredicate predicate;

	public CandidatePredicate(String value, double... scores) {
		this.scores = scores;
		this.predicate = new RDFPredicate(value);
	}

	public String value() {
		return predicate.uri();
	}
	
	public String label() {
		return predicate.label();
	}
	
	public double score(){
		return scores[0];
	}
	
	@Override
	public String toString() {
		return value() + " [" + asString(this.scores) + "]";
	}

	@Override
	public int compareTo(CandidatePredicate other) {
		return (int) Math.signum(other.score() - this.score());
	}
	
	private String asString(double... scores){
		String result = "";
		for(double score : scores){
			result += " " + score;
		}
		return result.trim();
	}
}