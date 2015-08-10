package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.index.Constraint;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;

import java.util.HashMap;

public class IndexTestDouble implements Index{

	private HashMap<String, CandidateResources> results = new HashMap<String, CandidateResources>();
	
	@Override
	public CandidateResources get(ContextualizedValues request, Constraint asQuery) throws Exception {
		CandidateResources result = results.get(request.first());
		if(result == null) result = new CandidateResources();
		return result;
	}

	@Override
	public long count(Constraint asQuery) throws Exception {
		return 0;
	}
	
	public IndexTestDouble resultFor(String value, String predicate, double score){
		if(!results.containsKey(value)) results.put(value, new CandidateResources());
		results.get(value).get(new CandidateProperty(predicate)).sumScore(score);
		return this;
	}
}