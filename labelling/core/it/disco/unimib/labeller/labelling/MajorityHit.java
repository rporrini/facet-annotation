package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityHit implements AnnotationAlgorithm{
	
	private Index index;
	private Discriminacy externalWeight;
	private Discriminacy internalWeight;
	private SelectionCriterion selection;

	public MajorityHit(Index index, SelectionCriterion criterion, Discriminacy externalWeight, Discriminacy internalWeight) {
		this.index = index;
		this.selection = criterion;
		this.externalWeight = externalWeight;
		this.internalWeight = internalWeight;
	}

	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		CandidatePredicatesReport predicates = new CandidatePredicatesReport(new CandidatePredicates(index));
		Distribution distribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), selection));
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		HashMap<String, Long> cachedFrequencyOfPredicates = new HashMap<String, Long>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!predicateCounts.containsKey(predicate)) predicateCounts.put(predicate, 0.0);
				long frequencyOfPredicate = index.count(predicate, context, new NoContext());
				cachedFrequencyOfPredicates.put(predicate, frequencyOfPredicate);
				double predicateAndValueDiscriminacy = internalWeight.of(predicate, value, frequencyOfPredicate, distribution);
				predicateCounts.put(predicate, predicateCounts.get(predicate) + (distribution.scoreOf(predicate, value) * predicateAndValueDiscriminacy));
			}
		}
		for(String predicate : predicateCounts.keySet()){
			double predicateAndContextDiscriminacy = externalWeight.of(predicate, context, cachedFrequencyOfPredicates.get(predicate), distribution);
			results.add(new CandidatePredicate(predicate, predicateCounts.get(predicate) * predicateAndContextDiscriminacy));
		}
		
		Collections.sort(results);
		return results;
	}

}
