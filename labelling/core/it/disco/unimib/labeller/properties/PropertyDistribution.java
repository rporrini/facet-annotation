package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PropertyDistribution{
	
	private HashMap<String, CandidateResourceSet> valueDistribution;
	private HashSet<String> properties;
	
	public PropertyDistribution(HashMap<String, CandidateResourceSet> valueDistribution) {
		this.valueDistribution = valueDistribution;
		this.properties = propertiesFrom(valueDistribution);
	}

	public double scoreOf(String property, String value) {
		return getOrDefault(property, value).score();
	}

	public TypeDistribution rangesOf(String property){
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		for(String value : valueDistribution.keySet()){
			CandidateResource resource = getOrDefault(property, value);
			for(CandidateResource object : resource.objectTypes()){
				if(!distribution.containsKey(object.id())) distribution.put(object.id(), 0.0);
				double delta = (object.score() / resource.totalOccurrences()) / (double)valueDistribution.size();
				distribution.put(object.id(), distribution.get(object.id()) + delta);
			}
		}
		TypeDistribution d = new TypeDistribution();
		for(String type : distribution.keySet()){
			d.trackTypeOccurrence(new String[]{type, distribution.get(type).toString()});
		}
		return d;
	}
	
	public Set<String> domainsOf(String property) {
		HashSet<String> result = new HashSet<String>();
		for(String value : values()){
			CandidateResource resource = getOrDefault(property, value);
			for(CandidateResource subject : resource.subjectTypes()){
				result.add(subject.id());
			}
		}
		return result;
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
		return valueDistribution.keySet();
	}
	
	private CandidateResource getOrDefault(String property, String value) {
		return valueDistribution.get(value).get(new CandidateResource(property));
	}
	
	private HashSet<String> propertiesFrom(HashMap<String, CandidateResourceSet> valueDistribution) {
		HashSet<String> hashSet = new HashSet<String>();
		for(String value : valueDistribution.keySet()) for(CandidateResource resource : valueDistribution.get(value).asList()) hashSet.add(resource.id());
		return hashSet;
	}
}