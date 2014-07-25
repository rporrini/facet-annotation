package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContextualizedOccurrences implements Occurrences{
	
	private HashMap<String, Double> scores;
	private SimilarityMetric metric;
	
	public ContextualizedOccurrences(SimilarityMetric metric){
		clear();
		this.metric = metric;
	}
	
	@Override
	public void accumulate(String label, String context, String targetContext){
		if(!scores.containsKey(label)) scores.put(label, 0.0);
		float similarity = metric.getSimilarity(targetContext, context);
		scores.put(label, scores.get(label) + similarity);
	}
	
	@Override
	public List<CandidatePredicate> toResults(){
		List<CandidatePredicate> annotations = new ArrayList<CandidatePredicate>();
		for(String label : scores.keySet()){
			annotations.add(new CandidatePredicate(label, scores.get(label)));
		}
		return annotations;
	}
	
	@Override
	public void clear(){
		this.scores = new HashMap<String, Double>();
	}
}