package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.HashMap;

public class CandidateProperties implements Properties{

	private Index index;

	public CandidateProperties(Index index) {
		this.index = index;
	}

	@Override
	public Distribution forValues(ContextualizedValues request, SelectionCriterion query) throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		for(ContextualizedValues singleRequest: request.split()){
			results.put(singleRequest.first(), index.get(singleRequest, query.asQuery(singleRequest)));
		}
		return new Distribution(results);
	}
}
