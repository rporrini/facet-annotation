package it.disco.unimib.labeller.index;

public class AnnotationResult implements Comparable<AnnotationResult>{

	private String value;
	private String label;
	private double score;

	public AnnotationResult(String value, double score) {
		this.value = value;
		this.label = getLabel(value);
		this.score = score;
	}

	public String value() {
		return value;
	}
	
	public String label() {
		return label;
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
	
	private String getLabel(String value) {
		String label = value;
		for (int index = value.indexOf('/'); index >= 0; index = value.indexOf('/', index + 1)){
			   label = value.substring(index+1);
		}
		return label;
	}
}