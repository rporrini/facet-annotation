package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.Index;

import java.util.HashMap;
import java.util.List;

public class CandidatePredicates implements Predicates{

	private Index index;

	public CandidatePredicates(Index index) {
		this.index = index;
	}

	@Override
	public HashMap<String, List<AnnotationResult>> forValues(String context, String[] values, FullTextQuery query) throws Exception {
		HashMap<String, List<AnnotationResult>> results = new HashMap<String, List<AnnotationResult>>();
		for(String value : values){
			results.put(value, index.get(value, context, query));
		}
		return results;
	}
}
