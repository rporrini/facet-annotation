package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.ContextUnawareCandidatePredicates;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedMaximumLikelihood;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;

public class Try {

	public static void main(String[] args) throws Exception {
		FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null);
		HashMap<String, List<SearchResult>> results = new ContextUnawareCandidatePredicates(predicates).forValues("2000", "2001", "2002");
		Distribution distribution = new Distribution(results);
		NormalizedMaximumLikelihood maximumLikelihood = new NormalizedMaximumLikelihood(distribution);
		for(String predicate : distribution.predicates()) {
			double likelihood = maximumLikelihood.of(predicate);
			if(likelihood > 0.01) System.out.println(predicate + "|" + likelihood);
		}
		predicates.closeReader();
	}

}
