package it.disco.unimib.labeller.index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;


public class CandidateResource implements Comparable<CandidateResource>{

	private double score;
	private double count;
	private RDFResource resource;
	
	private HashMap<String, Double> subjectTypes;
	private HashMap<String, Double> objectTypes;

	public CandidateResource(String id) {
		this.count = 0;
		this.score = 0;
		this.resource = new RDFResource(id);
		this.subjectTypes = new HashMap<String, Double>();
		this.objectTypes = new HashMap<String, Double>();
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
	
	public double totalOccurrences() {
		return this.count;
	}
	
	public void occurred() {
		this.count++;
	}
	
	public CandidateResource sumScore(double localScore) {
		this.score += localScore;
		return this;
	}
	
	public CandidateResource multiplyScore(double localScore) {
		double totalScore = this.score;
		if(totalScore == 0) totalScore = 1;
		this.score = totalScore * localScore;
		return this;
	}
	
	public CandidateResource addSubjectTypes(String... types) {
		addOrIncrementFrequencyCount(this.subjectTypes, types);
		return this;
	}

	public Collection<CandidateResource> subjectTypes() {
		return valuesOf(this.subjectTypes);
	}

	public CandidateResource addObjectTypes(String... types) {
		addOrIncrementFrequencyCount(this.objectTypes, types);
		return this;
	}

	public Collection<CandidateResource> objectTypes() {
		return valuesOf(this.objectTypes);
	}

	@Override
	public String toString() {
		return id() + " " + this.score;
	}

	@Override
	public int compareTo(CandidateResource other) {
		return (int) Math.signum(other.score() - this.score());
	}
	
	private void addOrIncrementFrequencyCount(HashMap<String, Double> frequencies, String... types) {
		for(String type : types){
			if(!frequencies.containsKey(type)) frequencies.put(type, 0.0);
			frequencies.put(type, frequencies.get(type) + 1.0);
		}
	}
	
	private Collection<CandidateResource> valuesOf(HashMap<String, Double> types) {
		CandidateResourceSet results = new CandidateResourceSet();
		for(Entry<String, Double> entry : types.entrySet()){
			results.get(new CandidateResource(entry.getKey()).sumScore(entry.getValue()));
		}
		return results.asList();
	}
}