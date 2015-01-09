package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.HashMap;

public class IndexTestDouble implements Index{

	private HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
	
	@Override
	public CandidateResourceSet get(ContextualizedValues request, TripleSelectionCriterion query) throws Exception {
		CandidateResourceSet result = results.get(request.first());
		if(result == null) result = new CandidateResourceSet();
		return result;
	}

	@Override
	public long countPredicatesInContext(ContextualizedValues request, TripleSelectionCriterion query) throws Exception {
		return 0;
	}
	
	public IndexTestDouble resultFor(String value, String predicate, double score){
		if(!results.containsKey(value)) results.put(value, new CandidateResourceSet());
		results.get(value).get(new CandidateResource(predicate)).sumScore(score);
		return this;
	}
}