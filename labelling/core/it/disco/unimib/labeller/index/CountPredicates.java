package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountPredicates implements Score{
	
	private HashMap<String, Double> scores;
	
	public CountPredicates(){
		clear();
	}
	
	@Override
	public void accumulate(String label, String context){
		if(!scores.containsKey(label)) scores.put(label, 0.0);
		scores.put(label, scores.get(label) + 1);
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