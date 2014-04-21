package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexTestDouble implements Index{

	private HashMap<String, List<SearchResult>> results = new HashMap<String, List<SearchResult>>();
	
	@Override
	public List<SearchResult> get(String type, String context) throws Exception {
		List<SearchResult> result = results.get(type);
		if(result == null) result = new ArrayList<SearchResult>();
		return result;
	}
	
	public IndexTestDouble resultFor(String type, String predicate, double score){
		if(!results.containsKey(type)) results.put(type, new ArrayList<SearchResult>());
		results.get(type).add(new SearchResult(predicate, score));
		return this;
	}
}