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
		
		logOccurrenciesByValue(values);
		logOccurrenciesByPredicate(values);
		log(distribution);
		
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
		return results;
	}

	private void logOccurrenciesByPredicate(HashMap<String, List<AnnotationResult>> values) {
		Events events = new Events();
		HashSet<String> predicates = new HashSet<String>();
		events.debug("Predicate|Values|Number of values|Average score");
		for(String value : values.keySet()){
			for(AnnotationResult predicate : values.get(value)){
				predicates.add(predicate.label());
			}
		}
		for(String predicate : predicates){
			String log = predicate + "|";
			int count = 0;
			double sum = 0;
			for(String value : values.keySet()){
				for(AnnotationResult result : values.get(value)){
					if(predicate.equals(result.label())){
						log += value + "; ";
						sum += result.score();
						count++;
					}
				}
			}
			events.debug(log + "|" + count + "|" + sum/count);
		}
	}

	private void logOccurrenciesByValue(HashMap<String, List<AnnotationResult>> values) {
		Events events = new Events();
		events.debug("Value (Number of predicates)|Predicates|Score");
		for(String value : values.keySet()){
			events.debug(value + " (" + values.get(value).size() + ")||");
			for(AnnotationResult result : values.get(value)){
				events.debug("|" + result.label() + "|" + result.score());
			}
		}
	}
	
	private void log(Distribution distribution) {
		Events events = new Events();
		events.debug("Got " + distribution.predicates().size() + " predicates over " + distribution.values().size() + " values");
		for(String predicate : distribution.predicates()){
			double score = 0.0;
			for(String value : distribution.values()){
				score+=distribution.scoreOf(predicate, value);
			}
			events.debug(predicate + " " + score);
		}
	}
}