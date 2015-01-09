package it.disco.unimib.labeller.index;




public class ContextualizedOccurrences{
	
	private String domain;
	private CandidateResourceSet scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric, String facetDomain){
		this.scores = new CandidateResourceSet();
		this.metric = metric;
		this.domain = facetDomain;
	}
	
	public void accumulate(String property, String context, String[] subjectTypes, String[] objectTypes){
		CandidateResource candidateResource = this.scores.get(new CandidateResource(property));
		candidateResource.occurred();
		candidateResource.sumScore(this.metric.getSimilarity(this.domain, context));
		candidateResource.addSubjectTypes(subjectTypes);
		candidateResource.addObjectTypes(objectTypes);
	}
	
	public CandidateResourceSet asResults() {
		return scores;
	}
}