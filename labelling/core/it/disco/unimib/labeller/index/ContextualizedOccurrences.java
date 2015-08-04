package it.disco.unimib.labeller.index;

public class ContextualizedOccurrences{
	
	private String domain;
	private CandidateResources scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric, String facetDomain){
		this.scores = new CandidateResources();
		this.metric = metric;
		this.domain = facetDomain;
	}
	
	public void accumulate(String property, String context, String[] subjectTypes, String[] objectTypes){
		CandidateProperty candidateResource = this.scores.get(new CandidateProperty(property));
		candidateResource.occurred();
		candidateResource.sumScore(this.metric.getSimilarity(this.domain, context));
		candidateResource.addDomains(subjectTypes);
		candidateResource.addRanges(objectTypes);
	}
	
	public CandidateResources asResults() {
		return scores;
	}
}