package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.OptionalContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MajorityHitWeighted implements AnnotationAlgorithm{
	
	private Index index;
	private PredicateWeight externalWeight;
	private PredicateWeight internalWeight;

	public MajorityHitWeighted(Index index, PredicateWeight externalWeight, PredicateWeight internalWeight) {
		this.index = index;
		this.externalWeight = externalWeight;
		this.internalWeight = internalWeight;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		InspectedPredicates predicates = new InspectedPredicates(new CandidatePredicates(index));
		Distribution distribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new OptionalContext()));
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		
		for(String predicate : distribution.predicates()){
			double score = 0;
			long frequencyOfPredicate = index.count(predicate, context, new OptionalContext());
			double predicateAndContextDiscriminacy = externalWeight.discriminacy(predicate, context, frequencyOfPredicate, null);
			for(String value : distribution.values()){
				double predicateAndValueDiscriminacy = internalWeight.discriminacy(predicate, value, frequencyOfPredicate, distribution);
				score += distribution.scoreOf(predicate, value) * predicateAndValueDiscriminacy;
			}
			results.add(new AnnotationResult(predicate, score * predicateAndContextDiscriminacy));
		}
		Collections.sort(results);
		return results;
	}

}
