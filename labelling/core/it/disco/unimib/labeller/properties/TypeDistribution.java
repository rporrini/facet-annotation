package it.disco.unimib.labeller.properties;

import java.util.HashMap;
import java.util.Set;

public class TypeDistribution {

	private HashMap<String, Double[]> frequencies;
	private double propertyFrequency;

	public TypeDistribution() {
		this.frequencies = new HashMap<String, Double[]>();
		this.propertyFrequency = 0;
	}

	public TypeDistribution trackTypeOccurrence(String type, String occurrence) {
		trackAs(type, occurrence, typeOccurrence());
		return this;
	}

	public TypeDistribution trackPropertyOccurrenceForType(String type, String occurrence) {
		trackAs(type, occurrence, propertyOccurrenceForType());
		return this;
	}
	
	public void trackPropertyOccurrence(String occurrence) {
		propertyFrequency = Double.parseDouble(occurrence);
	}

	public int size() {
		return all().size();
	}

	public HashMap<String, Double> getTypeFrequencies() {
		HashMap<String, Double> result = new HashMap<String, Double>();
		for(String type : all()){
			result.put(type, get(type, typeOccurrence()));
		}
		return result;
	}

	public Double typeOccurrence(String type) {
		return get(type, typeOccurrence());
	}
	
	public Double propertyOccurrenceForType(String type) {
		return get(type, propertyOccurrenceForType());
	}
	
	public Double propertyOccurrence() {
		return this.propertyFrequency;
	}

	public Set<String> all() {
		return frequencies.keySet();
	}
	
	private int typeOccurrence() {
		return 0;
	}
	
	private int propertyOccurrenceForType() {
		return 1;
	}
	
	private void trackAs(String type, String occurrence, int index) {
		if (!frequencies.containsKey(type)) frequencies.put(type, new Double[]{0.0, 0.0});
		frequencies.get(type)[index] += Double.parseDouble(occurrence);
	}
	
	private Double get(String type, int index) {
		Double[] frequencies = this.frequencies.get(type);
		double frequency = 0.0;
		if (frequencies != null) frequency = frequencies[index];
		return frequency;
	}
}