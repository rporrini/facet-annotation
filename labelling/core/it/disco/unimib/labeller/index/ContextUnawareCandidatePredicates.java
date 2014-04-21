package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.List;


public class ContextUnawareCandidatePredicates {

	private Index index;

	public ContextUnawareCandidatePredicates(Index index) {
		this.index = index;
	}

	public HashMap<String, List<SearchResult>> forValues(String... values) throws Exception {
		HashMap<String, List<SearchResult>> results = new HashMap<String, List<SearchResult>>();
		for(String value : values){
			results.put(value, index.get(value, "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"));
		}
		return results;
	}

}
