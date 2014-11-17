package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.HashMap;
import java.util.List;

public class CandidatePredicates implements Predicates{

	private Index index;

	public CandidatePredicates(Index index) {
		this.index = index;
	}

	@Override
	public Distribution forValues(String context, String[] values, TripleSelectionCriterion query) throws Exception {
		HashMap<String, List<CandidatePredicate>> results = new HashMap<String, List<CandidatePredicate>>();
		for(String value : values){
			results.put(value, index.get(value, context, query));
		}
		return new Distribution(results);
	}
}
