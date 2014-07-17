package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.PartialContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContextualizedMaximumLikelihood implements AnnotationAlgorithm{

	private GroupBySearch index;

	public ContextualizedMaximumLikelihood(GroupBySearch index) {
		this.index = index;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		InspectedPredicates predicates = new InspectedPredicates(new CandidatePredicates(index));
		
		Distribution optionalDistribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new OptionalContext()));
		Distribution partialDistribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new PartialContext()));
		
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : optionalDistribution.predicates()){
			double score = 0;
			double frequencyOfPredicateInContext = index.count(predicate, context, new MandatoryContext());
			
			for(String value : optionalDistribution.values()){
				double frequencyOfValueAndPredicate = optionalDistribution.scoreOf(predicate, value);
				double frequencyOfValue = optionalDistribution.totalScoreOf(value);
				double frequencyOfValueInContext = partialDistribution.totalScoreOf(value);
				double numerator = frequencyOfValueAndPredicate * frequencyOfValueInContext;
				double denominator = frequencyOfValue * frequencyOfPredicateInContext;
				
				if(denominator != 0){
					score += Math.log((numerator/denominator) + 1.0);
				}
			}
			results.add(new AnnotationResult(predicate, score));
		}
		Collections.sort(results);
		return results;
	}
}
