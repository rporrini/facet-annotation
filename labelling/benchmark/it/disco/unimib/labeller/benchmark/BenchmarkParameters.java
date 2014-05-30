package it.disco.unimib.labeller.benchmark;

public class BenchmarkParameters{
	
	private String[] args;

	public BenchmarkParameters(String[] args) {
		this.args = args;
	}
	
	public String algorithm(){
		return args[0];
	}
	
	public String metricName(){
		return args[1];
	}
	
	public String context(){
		return args[2];
	}
	
	public double majorityK() {
		return Double.parseDouble(args.length > 3 ? args[3] : "0");
	}
}