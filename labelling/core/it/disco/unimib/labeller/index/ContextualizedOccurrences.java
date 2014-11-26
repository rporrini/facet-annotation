package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContextualizedOccurrences{
	
	private String domain;
	private HashMap<String, CandidateResource> scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric, String facetDomain){
		this.scores = new HashMap<String, CandidateResource>();
		this.metric = metric;
		this.domain = facetDomain;
	}
	
	public void accumulate(String predicate, String context, String[] subjectTypes, String[] objectTypes){
		if(!scores.containsKey(predicate)) scores.put(predicate, new CandidateResource(predicate));
		CandidateResource candidateResource = scores.get(predicate);
		candidateResource.sumScore(metric.getSimilarity(domain, context));
		candidateResource.addSubjectTypes(subjectTypes);
		candidateResource.addObjectTypes(objectTypes);
	}
	
	public List<CandidateResource> toResults(){
		return new ArrayList<CandidateResource>(scores.values());
	}
}