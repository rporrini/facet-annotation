package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MajorityHit implements AnnotationAlgorithm{

	private Predicates candidates;

	public MajorityHit(Predicates candidates){
		this.candidates = candidates;
	}
	
	@Override
	public List<AnnotationResult> typeOf(String context, List<String> elements) throws Exception {
		HashMap<String, List<AnnotationResult>> values = candidates.forValues(context, elements.toArray(new String[elements.size()]));
		HashMap<String, Double> predicateCounts = new HashMap<String, Double>();
		for(String value : values.keySet()){
			for(AnnotationResult result : values.get(value)){
				if(!predicateCounts.containsKey(result.value())) predicateCounts.put(result.value(), result.score());
				else predicateCounts.put(result.value(), predicateCounts.get(result.value()) + result.score());
			}
		}
		List<AnnotationResult> results = new ArrayList<AnnotationResult>();
		for(String predicate : predicateCounts.keySet()){
			results.add(new AnnotationResult(predicate, predicateCounts.get(predicate)));
		}
		Collections.sort(results);
		return results;
	}
}
