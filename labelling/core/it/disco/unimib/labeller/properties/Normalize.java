package it.disco.unimib.labeller.properties;


public class Normalize {

	private double sum;

	public Normalize(Double[] doubles) {
		this.sum = sum(doubles);
	}

	private double sum(Double[] values) {
		double sum = 0;
		for(double value : values){
			sum += value;
		}
		return sum;
	}

	public double value(double value) {
		return value/sum;
	}
}
