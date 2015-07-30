package it.disco.unimib.labeller.properties;

import java.util.HashMap;
import java.util.Set;

public class TypeDistribution {

	private HashMap<String, Double> typeFrequencies;
	private HashMap<String, Double> propertyFrequenciesForTypes;

	public TypeDistribution() {
		this.typeFrequencies = new HashMap<String, Double>();
		this.propertyFrequenciesForTypes = new HashMap<String, Double>();
	}

	public void trackTypeOccurrence(String[] splitted) {
		trackIn(splitted, typeFrequencies);
	}
	
	public void trackPropertyOccurrenceForType(String[] splitted) {
		trackIn(splitted, propertyFrequenciesForTypes);
	}

	private void trackIn(String[] splitted, HashMap<String, Double> frequencies) {
		String type = splitted[0];
		if (!frequencies.containsKey(type)) frequencies.put(type, 0.0);
		frequencies.put(type, frequencies.get(type) + Double.parseDouble(splitted[1]));
	}
	
	public int size() {
		return typeFrequencies.size();
	}

	public HashMap<String, Double> getTypeFrequencies() {
		return typeFrequencies;
	}

	public Double typeOccurrence(String type) {
		return getOrDefault(type, typeFrequencies);
	}

	public Double propertyOccurrenceForType(String type) {
		return getOrDefault(type, propertyFrequenciesForTypes);
	}
	
	private Double getOrDefault(String type, HashMap<String, Double> frequencies) {
		Double frequency = frequencies.get(type);
		if (frequency == null) frequency = 0.0;
		return frequency;
	}

	public Set<String> all() {
		return typeFrequencies.keySet();
	}
}