package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.HashMap;

public class CandidatePredicates implements Predicates{

	private Index index;

	public CandidatePredicates(Index index) {
		this.index = index;
	}

	@Override
	public Distribution forValues(AnnotationRequest request, TripleSelectionCriterion query) throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		for(String value : request.elements()){
			results.put(value, index.get(new AnnotationRequest(request.context(), new String[]{value}), query));
		}
		return new Distribution(results);
	}
}
