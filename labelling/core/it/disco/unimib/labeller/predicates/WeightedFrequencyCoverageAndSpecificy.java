package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WeightedFrequencyCoverageAndSpecificy implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private SelectionCriterion selection;

	public WeightedFrequencyCoverageAndSpecificy(Index index, SelectionCriterion criterion, Specificity predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateSpecificity = predicateDiscriminacy;
	}

	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicatesReport(new CandidatePredicates(index))
										.forValues(context, elements.toArray(new String[elements.size()]), selection);
		
		HashMap<String, Double> frequenciesOverValues = new HashMap<String, Double>();
		HashMap<String, Long> overallFrequencies = new HashMap<String, Long>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!frequenciesOverValues.containsKey(predicate)) {
					frequenciesOverValues.put(predicate, 0.0);
				}
				if(!overallFrequencies.containsKey(predicate)) {
					overallFrequencies.put(predicate, index.countPredicatesInContext(predicate, context, new NoContext()));
				}
				frequenciesOverValues.put(predicate, frequenciesOverValues.get(predicate) + (distribution.scoreOf(predicate, value)));
			}
		}
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : frequenciesOverValues.keySet()){
			double disc = predicateSpecificity.of(predicate, context, overallFrequencies.get(predicate));
			Double wfreq = frequenciesOverValues.get(predicate);
			double pfd = wfreq * disc;
			results.add(new CandidatePredicate(predicate, wfreq, disc, pfd));
		}
		
		Collections.sort(results);
		return results;
	}

}
