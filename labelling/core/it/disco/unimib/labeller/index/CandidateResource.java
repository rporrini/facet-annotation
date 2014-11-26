package it.disco.unimib.labeller.index;


public class CandidateResource implements Comparable<CandidateResource>{

	private double score;
	private RDFResource resource;

	public CandidateResource(String id) {
		this.score = 0;
		this.resource = new RDFResource(id);
	}

	public String id() {
		return resource.uri();
	}
	
	public String label() {
		return resource.label();
	}
	
	public double score(){
		return score;
	}
	
	public void sumScore(double d) {
		this.score += d;
	}
	
	@Override
	public String toString() {
		return id() + " [" + this.score + "]";
	}

	@Override
	public int compareTo(CandidateResource other) {
		return (int) Math.signum(other.score() - this.score());
	}
}