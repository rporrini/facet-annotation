package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ContextualizedMaximumLikelihood implements AnnotationAlgorithm{

	private GroupBySearch index;

	public ContextualizedMaximumLikelihood(GroupBySearch index) {
		this.index = index;
	}

	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = new CandidatePredicates(index)
																.forValues(context, elements.toArray(new String[elements.size()]), new OptionalContext());
		Distribution distribution = new Distribution(values);
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : distribution.predicates()){
			double score = 0;
			for(String value : distribution.values()){
				double frequencyOfValueAndPredicate = distribution.scoreOf(predicate, value);
				double frequencyOfValue = distribution.totalScoreOf(value);
				double frequencyOfPredicateInContext = index.count(predicate, context, new MandatoryContext());
				double frequencyOfValueInContext = new Distribution(new CandidatePredicates(index).forValues(context, 
																										elements.toArray(new String[elements.size()]), 
																										new MandatoryContext()))
											.totalScoreOf(value);
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
