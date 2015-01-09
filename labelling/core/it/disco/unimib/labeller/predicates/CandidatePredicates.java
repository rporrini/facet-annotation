package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.HashMap;

public class CandidatePredicates implements Predicates{

	private Index index;

	public CandidatePredicates(Index index) {
		this.index = index;
	}

	@Override
	public Distribution forValues(ContextualizedValues request, TripleSelectionCriterion query) throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		for(ContextualizedValues singleRequest: request.split()){
			results.put(singleRequest.first(), index.get(singleRequest, query));
		}
		return new Distribution(results);
	}
}
