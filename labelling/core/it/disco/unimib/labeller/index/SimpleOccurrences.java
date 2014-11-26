package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleOccurrences implements Occurrences{
	
	private HashMap<String, Double> scores;
	
	public SimpleOccurrences(){
		this.scores = new HashMap<String, Double>();
	}
	
	@Override
	public void accumulate(String label, String context, String targetContext, String[] subjectTypes, String[] objectTypes){
		if(!scores.containsKey(label)) scores.put(label, 0.0);
		scores.put(label, scores.get(label) + 1);
	}
	
	@Override
	public List<CandidateResource> toResults(){
		List<CandidateResource> annotations = new ArrayList<CandidateResource>();
		for(String label : scores.keySet()){
			annotations.add(new CandidateResource(label, scores.get(label)));
		}
		return annotations;
	}
	
	@Override
	public Occurrences clear(){
		return new SimpleOccurrences();
	}
}