package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.OptionalContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimplifiedMaximumLikelihood implements AnnotationAlgorithm{

	private GroupBySearch index;

	public SimplifiedMaximumLikelihood(GroupBySearch index) {
		this.index = index;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		InspectedPredicates predicates = new InspectedPredicates(new CandidatePredicates(index));
		
		Distribution optionalDistribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new OptionalContext()));
		
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : optionalDistribution.predicates()){
			double score = 0;
			double frequencyOfPredicate = index.count(predicate, context, new OptionalContext());
			for(String value : optionalDistribution.values()){
				double frequencyOfPredicateAndValue = optionalDistribution.scoreOf(predicate, value);
				if(frequencyOfPredicate != 0)
					score += Math.log((frequencyOfPredicateAndValue/frequencyOfPredicate) + 1);
			}
			results.add(new AnnotationResult(predicate, score));
		}
		Collections.sort(results);
		return results;
	}

}
