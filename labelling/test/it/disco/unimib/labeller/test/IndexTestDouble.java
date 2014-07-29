package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexTestDouble implements Index{

	private HashMap<String, List<CandidatePredicate>> results = new HashMap<String, List<CandidatePredicate>>();
	
	@Override
	public List<CandidatePredicate> get(String value, String context, SelectionCriterion query) throws Exception {
		List<CandidatePredicate> result = results.get(value);
		if(result == null) result = new ArrayList<CandidatePredicate>();
		return result;
	}

	@Override
	public long countPredicatesInContext(String predicate, String context, SelectionCriterion query) throws Exception {
		return 0;
	}
	
	public IndexTestDouble resultFor(String value, String predicate, double score){
		if(!results.containsKey(value)) results.put(value, new ArrayList<CandidatePredicate>());
		results.get(value).add(new CandidatePredicate(predicate, score));
		return this;
	}
}