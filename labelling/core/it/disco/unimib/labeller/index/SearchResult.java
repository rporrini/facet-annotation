package it.disco.unimib.labeller.index;

public class SearchResult{

	private String value;
	private double score;

	public SearchResult(String value, double score) {
		this.value = value;
		this.score = score;
	}

	public String value() {
		return value;
	}
	
	public double score(){
		return score;
	}
}