package it.disco.unimib.labeller.index;


public class AnnotationResult implements Comparable<AnnotationResult>{

	private double score;
	private RDFPredicate predicate;

	public AnnotationResult(String value, double score) {
		this.score = score;
		this.predicate = new RDFPredicate(value);
	}

	public String value() {
		return predicate.uri();
	}
	
	public String label() {
		return predicate.label();
	}
	
	public double score(){
		return score;
	}
	
	@Override
	public String toString() {
		return value() + " [" + score + "]";
	}

	@Override
	public int compareTo(AnnotationResult other) {
		return (int) Math.signum(other.score() - this.score());
	}
}