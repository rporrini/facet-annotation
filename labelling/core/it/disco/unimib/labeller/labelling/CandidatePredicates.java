package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.Index;

import java.util.HashMap;
import java.util.List;

public class CandidatePredicates implements Predicates{

	private Index index;

	public CandidatePredicates(Index index) {
		this.index = index;
	}

	@Override
	public HashMap<String, List<CandidatePredicate>> forValues(String context, String[] values, SelectionCriterion query) throws Exception {
		HashMap<String, List<CandidatePredicate>> results = new HashMap<String, List<CandidatePredicate>>();
		for(String value : values){
			results.put(value, index.get(value, context, query));
		}
		return results;
	}
}
