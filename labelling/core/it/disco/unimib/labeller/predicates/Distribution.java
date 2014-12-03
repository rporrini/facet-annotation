package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Distribution{
	
	private HashMap<String, CandidateResourceSet> valueDistribution;
	private HashSet<String> predicates;
	
	public Distribution(HashMap<String, CandidateResourceSet> valueDistribution) {
		this.valueDistribution = valueDistribution;
		this.predicates = predicatesFrom(valueDistribution);
	}

	public double scoreOf(String predicate, String value) {
		return getOrDefault(predicate, value).score();
	}

	public Map<String, Double> objectsOf(String predicate){
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		for(String value : valueDistribution.keySet()){
			CandidateResource resource = getOrDefault(predicate, value);
			for(CandidateResource object : resource.objectTypes()){
				if(!distribution.containsKey(object.id())) distribution.put(object.id(), 0.0);
				double delta = (object.score() / resource.totalOccurrences()) / (double)valueDistribution.size();
				distribution.put(object.id(), distribution.get(object.id()) + delta);
			}
		}
		return distribution;
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
		return predicates;
	}
	
	public Set<String> values(){
		return valueDistribution.keySet();
	}
	
	private CandidateResource getOrDefault(String predicate, String value) {
		return valueDistribution.get(value).get(new CandidateResource(predicate));
	}
	
	private Map<String, Double> countDistributionOf(CandidateResource resource, Collection<CandidateResource> types) {
		HashMap<String, Double> distribution = new HashMap<String, Double>();
		for(CandidateResource subject : types){
			distribution.put(subject.id(), subject.score() / resource.totalOccurrences());
		}
		return distribution;
	}
	
	private HashSet<String> predicatesFrom(HashMap<String, CandidateResourceSet> valueDistribution) {
		HashSet<String> hashSet = new HashSet<String>();
		for(String value : valueDistribution.keySet())
			for(CandidateResource resource : valueDistribution.get(value).asList())
				hashSet.add(resource.id());
		return hashSet;
	}
}