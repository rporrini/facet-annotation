package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MaximumLikelihoodPredicate implements AnnotationAlgorithm{

	private CandidatePredicates candidates;

	public MaximumLikelihoodPredicate(CandidatePredicates candidates){
		this.candidates = candidates;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]));
		Distribution distribution = new Distribution(values);
		
		logHeader(context, distribution);
		logOccurrenciesByValue(values);
		logOccurrenciesByPredicate(values);
		
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(distribution);
		NormalizedPrior prior = new NormalizedPrior(distribution, unnormalizedPrior);
		
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(distribution, prior);
		NormalizedConditional conditional = new NormalizedConditional(distribution, prior, unnormalizedConditional);
		
		NormalizedMaximumLikelihood likelihood = new NormalizedMaximumLikelihood(distribution, conditional, prior);
		List<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : distribution.predicates()){
			results.add(new AnnotationResult(predicate, likelihood.of(predicate)));
		}
		Collections.sort(results);
		
		new Events().debug(results);
		
		return results;
	}

	private void logHeader(String context, Distribution distribution) {
		new Events().debug("CONTEXT|VALUES|PREDICATES");
		new Events().debug(context + "|" + distribution.values().size() + "|" + distribution.predicates().size());
	}

	private void logOccurrenciesByPredicate(HashMap<String, List<AnnotationResult>> values) {
		HashSet<String> predicates = new HashSet<String>();
		new Events().debug("Predicate|Values|Number of values|Average score");
		for(String value : values.keySet()){
			for(AnnotationResult predicate : values.get(value)){
				predicates.add(predicate.value());
			}
		}
		for(String predicate : predicates){
			String log = predicate + "|";
			int count = 0;
			double sum = 0;
			for(String value : values.keySet()){
				for(AnnotationResult result : values.get(value)){
					if(predicate.equals(result.value())){
						log += value + "; ";
						sum += result.score();
						count++;
					}
				}
			}
			new Events().debug(log + "|" + count + "|" + sum/count);
		}
	}

	private void logOccurrenciesByValue(HashMap<String, List<AnnotationResult>> values) {
		new Events().debug("Value (Number of predicates)|Predicates|Score");
		for(String value : values.keySet()){
			new Events().debug(value + " (" + values.get(value).size() + ")||");
			for(AnnotationResult result : values.get(value)){
				new Events().debug("|" + result.value() + "|" + result.score());
			}
		}
	}
}