package it.disco.unimib.labeller.index;



public class CandidateResource implements Comparable<CandidateResource>{

	private double[] scores;
	private RDFResource resource;

	public CandidateResource(String id, double... scores) {
		this.scores = scores;
		this.resource = new RDFResource(id);
	}

	public String id() {
		return resource.uri();
	}
	
	public String label() {
		return resource.label();
	}
	
	public double score(){
		return scores[scores.length - 1];
	}
	
	@Override
	public String toString() {
		return id() + " [" + asString(this.scores) + "]";
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