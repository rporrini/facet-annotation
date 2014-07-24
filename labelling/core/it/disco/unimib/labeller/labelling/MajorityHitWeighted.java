package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.OptionalContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MajorityHitWeighted implements AnnotationAlgorithm{
	
	private GroupBySearch index;
	private PredicateWeight predicateWeight;

	public MajorityHitWeighted(GroupBySearch index) {
		this.index = index;
		this.predicateWeight = new PredicateWeight();
	}
	
	public MajorityHitWeighted(GroupBySearch index, PredicateWeight predicateWeight) {
		this.index = index;
		this.predicateWeight = predicateWeight;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		InspectedPredicates predicates = new InspectedPredicates(new CandidatePredicates(index));
		Distribution distribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new OptionalContext()));
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		
		for(String predicate : distribution.predicates()){
			double score = 0;
			double predicateInContextDiscriminacy = predicateWeight.predicateInContextDiscriminacy(predicate, context);
			for(String value : distribution.values()){
				double predicateAndValueDiscriminacy = predicateWeight.predicateAndValueDiscriminacy(predicate, value, distribution);
				score += distribution.scoreOf(predicate, value) * predicateAndValueDiscriminacy;
			}
			results.add(new AnnotationResult(predicate, score * predicateInContextDiscriminacy));
		}
		Collections.sort(results);
		return results;
	}

}
