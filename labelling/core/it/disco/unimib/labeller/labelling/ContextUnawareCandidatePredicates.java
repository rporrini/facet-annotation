package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SearchResult;

import java.util.HashMap;
import java.util.List;


public class ContextUnawareCandidatePredicates {

	private Index index;

	public ContextUnawareCandidatePredicates(Index index) {
		this.index = index;
	}

	public HashMap<String, List<SearchResult>> forValues(String context, String[] values) throws Exception {
		HashMap<String, List<SearchResult>> results = new HashMap<String, List<SearchResult>>();
		for(String value : values){
			results.put(value, index.get(value, notExistingContext()));
		}
		return results;
	}

	private String notExistingContext() {
		return "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk";
	}

}
