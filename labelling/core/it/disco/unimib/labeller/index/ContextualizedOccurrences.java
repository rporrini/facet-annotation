package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContextualizedOccurrences{
	
	private HashMap<String, Double> scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric){
		this.scores = new HashMap<String, Double>();
		this.metric = metric;
	}
	
	public void accumulate(String label, String context, String targetContext, String[] subjectTypes, String[] objectTypes){
		if(!scores.containsKey(label)) scores.put(label, 0.0);
		float similarity = metric.getSimilarity(targetContext, context);
		scores.put(label, scores.get(label) + similarity);
	}
	
	public List<CandidateResource> toResults(){
		List<CandidateResource> annotations = new ArrayList<CandidateResource>();
		for(String label : scores.keySet()){
			annotations.add(new CandidateResource(label, scores.get(label)));
		}
		return annotations;
	}
}