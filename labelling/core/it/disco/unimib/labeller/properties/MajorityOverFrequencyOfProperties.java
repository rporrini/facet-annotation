package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityOverFrequencyOfProperties implements AnnotationAlgorithm{
	
	private Index index;
	private SelectionCriterion selection;

	public MajorityOverFrequencyOfProperties(Index index, SelectionCriterion criterion) {
		this.index = index;
		this.selection = criterion;
	}

	@Override
	public List<CandidateResource> annotate(ContextualizedValues request) throws Exception {
		
		PropertyDistribution distribution = new CandidateProperties(index).forValues(request, selection);
		
		HashMap<String, Double> propertyCounts = new HashMap<String, Double>();
		
		for(String value : distribution.values()){
			for(String property : distribution.properties()){
				if(!propertyCounts.containsKey(property)) {
					propertyCounts.put(property, 0.0);
				}
				propertyCounts.put(property, propertyCounts.get(property) + (distribution.scoreOf(property, value)));
			}
		}
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String property : propertyCounts.keySet()){
			Double wfreq = propertyCounts.get(property);
			CandidateResource e = new CandidateResource(property);
			e.sumScore(wfreq);
			results.add(e);
		}
		
		Collections.sort(results);
		return results;
	}

}