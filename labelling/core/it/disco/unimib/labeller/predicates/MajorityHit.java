package it.disco.unimib.labeller.predicates;

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
	private Discriminacy predicateDiscriminacy;
	private Discriminacy valueDiscriminacy;
	private SelectionCriterion selection;

	public MajorityHit(Index index, SelectionCriterion criterion, Discriminacy valueDiscriminacy, Discriminacy predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateDiscriminacy = predicateDiscriminacy;
		this.valueDiscriminacy = valueDiscriminacy;
	}

	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicatesReport(new CandidatePredicates(index)).forValues(context, elements.toArray(new String[elements.size()]), selection);
		
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		HashMap<String, Long> cachedFrequencyOfPredicates = new HashMap<String, Long>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!predicateCounts.containsKey(predicate)) {
					predicateCounts.put(predicate, 0.0);
				}
				if(!cachedFrequencyOfPredicates.containsKey(predicate)) {
					cachedFrequencyOfPredicates.put(predicate, index.countPredicatesInContext(predicate, context, new NoContext())); 
				}
				long frequencyOfPredicate = cachedFrequencyOfPredicates.get(predicate); 
				
				double predicateAndValueDiscriminacy = valueDiscriminacy.of(predicate, value, frequencyOfPredicate);
				predicateCounts.put(predicate, predicateCounts.get(predicate) + (distribution.scoreOf(predicate, value) * predicateAndValueDiscriminacy));
			}
		}
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : predicateCounts.keySet()){
			double disc = predicateDiscriminacy.of(predicate, context, cachedFrequencyOfPredicates.get(predicate));
			Double wfreq = predicateCounts.get(predicate);
			double pfd = wfreq * disc;
			results.add(new CandidatePredicate(predicate, wfreq, disc, pfd));
		}
		
		Collections.sort(results);
		return results;
	}

}
