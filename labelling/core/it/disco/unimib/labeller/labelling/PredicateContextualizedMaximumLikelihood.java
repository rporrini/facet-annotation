package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PredicateContextualizedMaximumLikelihood implements AnnotationAlgorithm{

	private GroupBySearch index;

	public PredicateContextualizedMaximumLikelihood(GroupBySearch index) {
		this.index = index;
	}

	@Override
	public List<CandidatePredicate> typeOf(String context, List<String> elements) throws Exception {
		CandidatePredicatesReport predicates = new CandidatePredicatesReport(new CandidatePredicates(index));
		
		Distribution optionalDistribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new NoContext()));
		Distribution partialDistribution = new Distribution(predicates.forValues(context, elements.toArray(new String[elements.size()]), new PartialContext()));
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		for(String predicate : optionalDistribution.predicates()){
			double score = 0;
			double frequencyOfPredicateInContext = index.countPredicatesInContext(predicate, context, new CompleteContext());
			
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
			results.add(new CandidatePredicate(predicate, score));
		}
		Collections.sort(results);
		return results;
	}
}
