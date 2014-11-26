package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContextualizedOccurrences{
	
	private String domain;
	private HashMap<String, Double> scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric, String facetDomain){
		this.scores = new HashMap<String, Double>();
		this.metric = metric;
		this.domain = facetDomain;
	}
	
	public void accumulate(String predicate, String context, String[] subjectTypes, String[] objectTypes){
		if(!scores.containsKey(predicate)) scores.put(predicate, 0.0);
		float similarity = metric.getSimilarity(domain, context);
		scores.put(predicate, scores.get(predicate) + similarity);
	}
	
	public List<CandidateResource> toResults(){
		List<CandidateResource> annotations = new ArrayList<CandidateResource>();
		for(String label : scores.keySet()){
			CandidateResource e = new CandidateResource(label);
			e.sumScore(scores.get(label));
			annotations.add(e);
		}
		return annotations;
	}
}