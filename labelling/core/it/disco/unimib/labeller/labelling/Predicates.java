package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.Index;

import java.util.HashMap;
import java.util.List;


public class Predicates implements CandidatePredicates {

	private Index index;

	public Predicates(Index index) {
		this.index = index;
	}

	@Override
	public HashMap<String, List<AnnotationResult>> forValues(String context, String[] values) throws Exception {
		HashMap<String, List<AnnotationResult>> results = new HashMap<String, List<AnnotationResult>>();
		for(String value : values){
			results.put(value, index.get(value, context));
		}
		return results;
	}
}
