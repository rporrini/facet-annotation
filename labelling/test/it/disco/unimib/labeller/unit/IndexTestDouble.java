package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexTestDouble implements Index{

	private HashMap<String, List<CandidateResource>> results = new HashMap<String, List<CandidateResource>>();
	
	@Override
	public List<CandidateResource> get(String value, String context, TripleSelectionCriterion query) throws Exception {
		List<CandidateResource> result = results.get(value);
		if(result == null) result = new ArrayList<CandidateResource>();
		return result;
	}

	@Override
	public long countPredicatesInContext(String predicate, String context, TripleSelectionCriterion query) throws Exception {
		return 0;
	}
	
	public IndexTestDouble resultFor(String value, String predicate, double score){
		if(!results.containsKey(value)) results.put(value, new ArrayList<CandidateResource>());
		CandidateResource e = new CandidateResource(predicate);
		e.sumScore(score);
		results.get(value).add(e);
		return this;
	}
}