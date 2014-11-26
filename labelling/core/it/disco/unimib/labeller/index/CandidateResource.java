package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;


public class CandidateResource implements Comparable<CandidateResource>{

	private double score;
	private List<Double> localScores;
	private RDFResource resource;

	public CandidateResource(String id) {
		this.score = 0;
		this.localScores = new ArrayList<Double>();
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
	
	public void sumScore(double localScore) {
		this.score += localScore;
		this.localScores.add(localScore);
	}
	
	public void multiplyScore(double localScore) {
		double totalScore = this.score;
		if(totalScore == 0) totalScore = 1;
		this.score = totalScore * localScore;
		this.localScores.add(localScore);
	}
	
	@Override
	public String toString() {
		ArrayList<Double> results = new ArrayList<Double>(this.localScores);
		results.add(this.score);
		return id() + " " + results;
	}

	@Override
	public int compareTo(CandidateResource other) {
		return (int) Math.signum(other.score() - this.score());
	}
}