package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;

public class WeightedPredicates implements Score{
	
	private HashMap<String, Double> scores;
	private AbstractStringMetric metric;
	
	public WeightedPredicates(AbstractStringMetric metric){
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
	public List<AnnotationResult> toResults(){
		List<AnnotationResult> annotations = new ArrayList<AnnotationResult>();
		for(String label : scores.keySet()){
			annotations.add(new AnnotationResult(label, scores.get(label)));
		}
		return annotations;
	}
	
	@Override
	public void clear(){
		this.scores = new HashMap<String, Double>();
	}
}