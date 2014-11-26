package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CandidateResource implements Comparable<CandidateResource>{

	private double score;
	private List<Double> localScores;
	private RDFResource resource;
	
	private HashMap<String, CandidateResource> subjectTypes;
	private HashMap<String, CandidateResource> objectTypes;

	public CandidateResource(String id) {
		this.score = 0;
		this.localScores = new ArrayList<Double>();
		this.resource = new RDFResource(id);
		this.subjectTypes = new HashMap<String, CandidateResource>();
		this.objectTypes = new HashMap<String, CandidateResource>();
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
	
	public void addSubjectTypes(String... types) {
		addOrIncrementFrequencyCount(this.subjectTypes, types);
	}

	public List<CandidateResource> subjectTypes() {
		return valuesOf(this.subjectTypes);
	}

	public void addObjectTypes(String... types) {
		addOrIncrementFrequencyCount(this.objectTypes, types);
	}

	public List<CandidateResource> objectTypes() {
		return valuesOf(this.objectTypes);
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
	
	private void addOrIncrementFrequencyCount(HashMap<String, CandidateResource> frequencies, String... types) {
		for(String type : types){
			if(!frequencies.containsKey(type)) frequencies.put(type, new CandidateResource(type));
			frequencies.get(type).sumScore(1.0);
		}
	}
	
	private ArrayList<CandidateResource> valuesOf(HashMap<String, CandidateResource> values) {
		return new ArrayList<CandidateResource>(values.values());
	}
}