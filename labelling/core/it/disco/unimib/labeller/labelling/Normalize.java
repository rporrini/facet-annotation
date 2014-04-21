package it.disco.unimib.labeller.labelling;


public class Normalize {

	private double max;

	public Normalize(double[] values) {
		this.max = max(values);
	}

	private double max(double[] values) {
		double maximum = 0;
		for(double value : values){
			if(maximum < value) maximum = value;
		}
		return maximum;
	}

	public double value(double value) {
		return value/max;
	}
}
