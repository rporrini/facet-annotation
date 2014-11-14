package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private SelectionCriterion selection;

	public WeightedFrequencyCoverageAndSpecificity(Index index, SelectionCriterion criterion, Specificity predicateDiscriminacy) {
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
		HashMap<String, Double> coveredValues = new HashMap<String, Double>();
		
		for(String value : distribution.values()){
			for(String predicate : distribution.predicates()){
				if(!frequenciesOverValues.containsKey(predicate)) frequenciesOverValues.put(predicate, 0.0);
				if(!overallFrequencies.containsKey(predicate)) overallFrequencies.put(predicate, index.countPredicatesInContext(predicate, context, new NoContext()));
				
				double score = distribution.scoreOf(predicate, value);
				frequenciesOverValues.put(predicate, frequenciesOverValues.get(predicate) + score);
				
				if(!coveredValues.containsKey(predicate)) coveredValues.put(predicate, 0.0);
				if(score > 0) coveredValues.put(predicate, coveredValues.get(predicate) + 1.0);
			}
		}
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : frequenciesOverValues.keySet()){
			double disc = predicateSpecificity.of(predicate, context, overallFrequencies.get(predicate));
			double smoothedWFreq = Math.log(frequenciesOverValues.get(predicate) + 1.000000001);
			double coverage = coveredValues.get(predicate) / (double)distribution.values().size();
			double pfd = smoothedWFreq * coverage * disc;
			results.add(new CandidatePredicate(predicate, smoothedWFreq, coverage, disc, pfd));
		}
		
		Collections.sort(results);
		return results;
	}
}
