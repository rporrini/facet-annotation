package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityOverFrequencyOfPredicates implements AnnotationAlgorithm{
	
	private Index index;
	private SelectionCriterion selection;

	public MajorityOverFrequencyOfPredicates(Index index, SelectionCriterion criterion) {
		this.index = index;
		this.selection = criterion;
	}

	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicatesReport(new CandidatePredicates(index))
											.forValues(context, elements.toArray(new String[elements.size()]), selection);
		
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!predicateCounts.containsKey(predicate)) {
					predicateCounts.put(predicate, 0.0);
				}
				predicateCounts.put(predicate, predicateCounts.get(predicate) + (distribution.scoreOf(predicate, value)));
			}
		}
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : predicateCounts.keySet()){
			Double wfreq = predicateCounts.get(predicate);
			results.add(new CandidatePredicate(predicate, wfreq));
		}
		
		Collections.sort(results);
		return results;
	}

}