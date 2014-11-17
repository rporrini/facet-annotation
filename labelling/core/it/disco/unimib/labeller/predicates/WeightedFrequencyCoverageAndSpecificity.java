package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private TripleSelectionCriterion selection;

	public WeightedFrequencyCoverageAndSpecificity(Index index, TripleSelectionCriterion criterion, Specificity predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateSpecificity = predicateDiscriminacy;
	}

	@Override
	public List<CandidatePredicate> typeOf(String domain, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicatesReport(new CandidatePredicates(index))
										.forValues(domain, elements.toArray(new String[elements.size()]), selection);
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : distribution.predicates()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(predicate, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
						
			long overallFrequency = index.countPredicatesInContext(predicate, domain, new NoContext());
			double disc = predicateSpecificity.of(predicate, domain, overallFrequency);
			double smoothedWFreq = Math.log(frequencyOverValues + 1.000000001);
			double coverage = covered / (double)distribution.values().size();
			double pfd = smoothedWFreq * coverage * disc;
			results.add(new CandidatePredicate(predicate, smoothedWFreq, coverage, disc, pfd));
		}
		
		Collections.sort(results);
		return results;
	}
}
