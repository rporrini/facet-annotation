package it.disco.unimib.labeller.index;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;

public class Try {

	public static void main(String[] args) throws Exception {
		FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null);
		HashMap<String, List<SearchResult>> results = new ContextUnawareCandidatePredicates(predicates).forValues("1999", "2001", "1866");
		for(String key : results.keySet()){
			System.out.println(key);
			for(SearchResult result : results.get(key)){
				System.out.println(result);
			}
		}
		predicates.closeReader();
	}

}
