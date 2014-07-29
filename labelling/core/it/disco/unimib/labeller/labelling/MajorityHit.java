package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
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
		
		for(String predicate : distribution.predicates()){
			double score = 0;
			long frequencyOfPredicate = index.count(predicate, context, new NoContext());
			double predicateAndContextDiscriminacy = externalWeight.of(predicate, context, frequencyOfPredicate, distribution);
			for(String value : distribution.values()){
				double predicateAndValueDiscriminacy = internalWeight.of(predicate, value, frequencyOfPredicate, distribution);
				score += distribution.scoreOf(predicate, value) * predicateAndValueDiscriminacy;
			}
			results.add(new CandidatePredicate(predicate, score * predicateAndContextDiscriminacy));
		}
		Collections.sort(results);
		return results;
	}

}
