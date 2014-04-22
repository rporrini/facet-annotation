package it.disco.unimib.labeller.index;

public class AnnotationResult implements Comparable<AnnotationResult>{

	private String value;
	private double score;

	public AnnotationResult(String value, double score) {
		this.value = value;
		this.score = score;
	}

	public String value() {
		return value;
	}
	
	public double score(){
		return score;
	}
	
	@Override
	public String toString() {
		return value + " [" + score + "]";
	}

	@Override
	public int compareTo(AnnotationResult other) {
		return (int) Math.signum(other.score() - this.score());
	}
}