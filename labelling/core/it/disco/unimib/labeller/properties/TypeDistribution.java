package it.disco.unimib.labeller.properties;

import java.util.HashMap;

public class TypeDistribution{

	private HashMap<String, Double> typeFrequencies;
	
	public TypeDistribution(){
		this.typeFrequencies = new HashMap<String, Double>();
	}
	
	public void track(String[] splitted) {
		typeFrequencies.put(splitted[0], Double.parseDouble(splitted[1]));
	}

	public int size() {
		return typeFrequencies.size();
	}
	
	public HashMap<String, Double> get(){
		return typeFrequencies;
	}
}