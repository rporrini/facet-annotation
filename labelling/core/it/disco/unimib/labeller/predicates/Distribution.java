package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;

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
		CandidateResource resource = scores.get(predicate).get(value);
		if(resource == null){
			return 0.0;
		}
		return resource.score();
	}
	
	public Map<String, Double> objectsOf(String predicate, String value) {
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		CandidateResource resource = scores.get(predicate).get(value);
		if(resource == null) return distribution;
		for(CandidateResource object : resource.objectTypes()){
			distribution.put(object.id(), object.score() / resource.totalOccurrences());
		}
		return distribution;
	}
	
	public Map<String, Double> subjectsOf(String predicate, String value) {
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		CandidateResource resource = scores.get(predicate).get(value);
		if(resource == null) return distribution;
		for(CandidateResource subject : resource.subjectTypes()){
			distribution.put(subject.id(), subject.score() / resource.totalOccurrences());
		}
		return distribution;
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