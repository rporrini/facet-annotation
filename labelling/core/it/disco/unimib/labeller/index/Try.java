package it.disco.unimib.labeller.index;

import java.io.File;

import org.apache.lucene.store.NIOFSDirectory;

public class Try {

	public static void main(String[] args) throws Exception {
		FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/predicates")), null, null);
		
		System.out.println(predicates.get("1956-10-11", "actors"));
		
		predicates.closeReader();
	}

}
