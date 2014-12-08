package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityOverFrequencyOfPredicates implements AnnotationAlgorithm{
	
	private Index index;
	private TripleSelectionCriterion selection;

	public MajorityOverFrequencyOfPredicates(Index index, TripleSelectionCriterion criterion) {
		this.index = index;
		this.selection = criterion;
	}

	@Override
	public List<CandidateResource> typeOf(String context, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicates(index).forValues(context, elements.toArray(new String[elements.size()]), selection);
		
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!predicateCounts.containsKey(predicate)) {
					predicateCounts.put(predicate, 0.0);
				}
				predicateCounts.put(predicate, predicateCounts.get(predicate) + (distribution.scoreOf(predicate, value)));
			}
		}
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : predicateCounts.keySet()){
			Double wfreq = predicateCounts.get(predicate);
			CandidateResource e = new CandidateResource(predicate);
			e.sumScore(wfreq);
			results.add(e);
		}
		
		Collections.sort(results);
		return results;
	}

}