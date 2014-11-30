package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Distribution{
	
	private HashMap<String, HashMap<String, CandidateResource>> scores;
	private Set<String> values;
	
	public Distribution(HashMap<String, CandidateResourceSet> valueDistribution) {
		this.scores = invert(valueDistribution);
		this.values = valueDistribution.keySet();
	}
	
	public double scoreOf(String predicate, String value) {
		return getOrDefault(predicate, value).score();
	}

	public Map<String, Double> objectsOf(String predicate, String value) {
		CandidateResource resource = getOrDefault(predicate, value);
		Collection<CandidateResource> types = resource.objectTypes();
		
		return countDistributionOf(resource, types);
	}
	
	public Map<String, Double> subjectsOf(String predicate, String value) {
		CandidateResource resource = getOrDefault(predicate, value);
		Collection<CandidateResource> types = resource.subjectTypes();
		
		return countDistributionOf(resource, types);
	}

	public double totalScoreOf(String value){
		double result = 0;
		for(String predicate : predicates()){
			result+=scoreOf(predicate, value);
		}
		return result;
	}
	
	public Set<String> predicates(){
		return scores.keySet();
	}
	
	public Set<String> values(){
		return values;
	}
	
	private CandidateResource getOrDefault(String predicate, String value) {
		HashMap<String, CandidateResource> values = scores.get(predicate);
		if(values == null) return new CandidateResource(predicate);
		CandidateResource resource = values.get(value);
		if(resource == null) return new CandidateResource(predicate);
		return resource;
	}
	
	private Map<String, Double> countDistributionOf(CandidateResource resource, Collection<CandidateResource> types) {
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		for(CandidateResource subject : types){
			distribution.put(subject.id(), subject.score() / resource.totalOccurrences());
		}
		return distribution;
	}
	
	private HashMap<String, HashMap<String, CandidateResource>> invert(HashMap<String, CandidateResourceSet> valueDistribution) {
		HashMap<String, HashMap<String, CandidateResource>> inverted = new HashMap<String, HashMap<String, CandidateResource>>();
		for(String value : valueDistribution.keySet()){
			for(CandidateResource predicate : valueDistribution.get(value).asList()){
				if(!inverted.containsKey(predicate.id())) {
					inverted.put(predicate.id(), new HashMap<String, CandidateResource>());
				}
				inverted.get(predicate.id()).put(value, predicate);
			}
		}
		return inverted;
	}
}