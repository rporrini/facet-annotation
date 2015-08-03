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

	public TypeDistribution rangesOf(String property){
		TypeDistribution distribution = new TypeDistribution();
		for(String value : candidatePropertiesForValues.keySet()){
			CandidateResource resource = getOrDefault(property, value);
			for(CandidateResource object : resource.ranges()){
				double delta = (object.score() / resource.totalOccurrences());
				distribution.trackPropertyOccurrenceForType(object.uri(), delta + "");
			}
		}
		return distribution;
	}
	
	public TypeDistribution domainsOf(String property) {
		TypeDistribution distribution = new TypeDistribution();
		for(String value : values()){
			for(CandidateResource subject : getOrDefault(property, value).domains()){
				distribution.trackPropertyOccurrenceForType(subject.uri(), subject.score() + "");
			}
		}
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