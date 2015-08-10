package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.properties.TypeDistribution;

import org.semanticweb.yars.nx.Resource;

public class CandidateProperty implements Comparable<CandidateProperty>{

	private double score;
	private double count;
	private RDFResource resource;
	
	private TypeDistribution domainTypes;
	private TypeDistribution rangeTypes;

	public CandidateProperty(String uri) {
		this.count = 0;
		this.score = 0;
		this.resource = new RDFResource(new Resource(uri));
		this.domainTypes = new TypeDistribution();
		this.rangeTypes = new TypeDistribution();
	}

	public String uri() {
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
	
	public CandidateProperty occurred() {
		this.count++;
		return this;
	}
	
	public CandidateProperty sumScore(double localScore) {
		this.score += localScore;
		return this;
	}
	
	public CandidateProperty multiplyScore(double localScore) {
		double totalScore = this.score;
		if(totalScore == 0) totalScore = 1;
		this.score = totalScore * localScore;
		return this;
	}
	
	public CandidateProperty addDomains(String... types) {
		trackOn(this.domainTypes, types);
		return this;
	}

	public TypeDistribution domains() {
		return this.domainTypes;
	}

	public CandidateProperty addRanges(String... types) {
		trackOn(this.rangeTypes, types);
		return this;
	}

	public TypeDistribution ranges() {
		return this.rangeTypes;
	}

	@Override
	public String toString() {
		return uri() + " " + this.score;
	}

	@Override
	public int compareTo(CandidateProperty other) {
		return (int) Math.signum(other.score() - this.score());
	}
	
	private void trackOn(TypeDistribution frequencies, String... types) {
		for(String type : types){
			frequencies.trackTypeOccurrence(type, 1 + "");
		}
	}
}