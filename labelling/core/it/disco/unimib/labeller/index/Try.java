package it.disco.unimib.labeller.index;

import java.io.File;

import org.apache.lucene.store.NIOFSDirectory;

public class Try {

	public static void main(String[] args) throws Exception {
		FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties")), null, null);
		
//		List<String> x = predicates.get("466", "kjhkjhkjjsjjsss");

//		System.out.println(x.size());
//		for(String property : x){ 
//			System.out.println(property);
//		}
		
		predicates.closeReader();
	}

}
