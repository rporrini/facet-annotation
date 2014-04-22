package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.ContextUnawareCandidatePredicates;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedConditional;
import it.disco.unimib.labeller.labelling.NormalizedPrior;

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
		NormalizedPrior prior = new NormalizedPrior(new Distribution(results));
		NormalizedConditional conditional = new NormalizedConditional(new Distribution(results));
		System.out.println(conditional.of("http://dbpedia.org/ontology/year", "1866") / prior.of("http://dbpedia.org/ontology/year"));
		System.out.println(conditional.of("http://dbpedia.org/ontology/foundingYear", "1866") / prior.of("http://dbpedia.org/ontology/foundingYear"));
		
		predicates.closeReader();
	}

}
