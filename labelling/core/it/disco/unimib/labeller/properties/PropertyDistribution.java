package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PropertyDistribution{
	
	private HashMap<String, CandidateResources> candidatePropertiesForValues;
	private HashSet<String> properties;
	
	public PropertyDistribution(HashMap<String, CandidateResources> valueDistribution) {
		this.candidatePropertiesForValues = valueDistribution;
		this.properties = propertiesFrom(valueDistribution);
	}

	public double scoreOf(String property, String value) {
		return getOrDefault(property, value).score();
	}

	public TypeDistribution rangesOf(String propertyUri){
		TypeDistribution distribution = new TypeDistribution();
		double totalOccurrence = 0.0;
		for(String value : values()){
			CandidateResource property = getOrDefault(propertyUri, value);
			for(CandidateResource range : property.ranges()){
				totalOccurrence += (double)property.totalOccurrences();
				distribution.trackPropertyOccurrenceForType(range.uri(), range.score() + "");
			}
		}
		distribution.trackPropertyOccurrence(totalOccurrence + "");
		return distribution;
	}
	
	public TypeDistribution domainsOf(String propertyUri) {
		TypeDistribution distribution = new TypeDistribution();
		double totalOccurrence = 0.0;
		for(String value : values()){
			CandidateResource property = getOrDefault(propertyUri, value);
			for(CandidateResource domain : property.domains()){
				totalOccurrence += (double)property.totalOccurrences();
				distribution.trackPropertyOccurrenceForType(domain.uri(), domain.score() + "");
			}
		}
		distribution.trackPropertyOccurrence(totalOccurrence + "");
		return distribution;
	}
	
	public double totalScoreOf(String value){
		double result = 0;
		for(String property : properties()){
			result+=scoreOf(property, value);
		}
		return result;
	}
	
	public Set<String> properties(){
		return properties;
	}
	
	public Set<String> values(){
		return candidatePropertiesForValues.keySet();
	}
	
	private CandidateResource getOrDefault(String property, String value) {
		return candidatePropertiesForValues.get(value).get(new CandidateResource(property));
	}
	
	private HashSet<String> propertiesFrom(HashMap<String, CandidateResources> valueDistribution) {
		HashSet<String> properties = new HashSet<String>();
		for(String value : valueDistribution.keySet()) {
			for(CandidateResource resource : valueDistribution.get(value).asList()) {
				properties.add(resource.uri());
			}
		}
		return properties;
	}
}