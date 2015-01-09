package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityOverCoveredValues implements AnnotationAlgorithm{

	private Index index;
	private double threshold;
	private SelectionCriterion query;

	public MajorityOverCoveredValues(Index candidates, double threshold, SelectionCriterion query){
		this.index = candidates;
		this.threshold = threshold;
		this.query = query;
	}
	
	@Override
	public List<CandidateResource> typeOf(ContextualizedValues request) throws Exception {
		Distribution values = new CandidateProperties(index).forValues(request, query);
		HashMap<String, Double> propertyCounts = new HashMap<String, Double>();
		for(String value : values.values()){
			for(String result : values.properties()){
				if(!propertyCounts.containsKey(result)) propertyCounts.put(result, 0.0);
				if(values.scoreOf(result, value) > 0) propertyCounts.put(result, propertyCounts.get(result) + 1);
			}
		}
		List<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String property : propertyCounts.keySet()){
			double count = propertyCounts.get(property);
			double percentage = count / (double) request.all().length;
			if(percentage > threshold){
				CandidateResource e = new CandidateResource(property);
				e.sumScore(percentage);
				results.add(e);
			}
		}
		Collections.sort(results);
		return results;
	}
}