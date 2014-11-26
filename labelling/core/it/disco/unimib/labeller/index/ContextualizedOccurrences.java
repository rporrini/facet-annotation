package it.disco.unimib.labeller.index;

import java.util.List;



public class ContextualizedOccurrences{
	
	private String domain;
	private CandidateResourceSet scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric, String facetDomain){
		this.scores = new CandidateResourceSet();
		this.metric = metric;
		this.domain = facetDomain;
	}
	
	public void accumulate(String predicate, String context, String[] subjectTypes, String[] objectTypes){
		CandidateResource candidateResource = scores.get(new CandidateResource(predicate));
		candidateResource.sumScore(metric.getSimilarity(domain, context));
		candidateResource.addSubjectTypes(subjectTypes);
		candidateResource.addObjectTypes(objectTypes);
	}
	
	public List<CandidateResource> toResults(){
		return scores.asList();
	}
}