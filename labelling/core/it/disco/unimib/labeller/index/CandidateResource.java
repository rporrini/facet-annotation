package it.disco.unimib.labeller.index;



public class CandidateResource implements Comparable<CandidateResource>{

	private double[] scores;
	private RDFResource predicate;

	public CandidateResource(String value, double... scores) {
		this.scores = scores;
		this.predicate = new RDFResource(value);
	}

	public String value() {
		return predicate.uri();
	}
	
	public String label() {
		return predicate.label();
	}
	
	public double score(){
		return scores[scores.length - 1];
	}
	
	@Override
	public String toString() {
		return value() + " [" + asString(this.scores) + "]";
	}

	@Override
	public int compareTo(CandidateResource other) {
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