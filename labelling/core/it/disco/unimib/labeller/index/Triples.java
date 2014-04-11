package it.disco.unimib.labeller.index;

import org.semanticweb.yars.nx.parser.NxParser;


public class Triples {

	private FileSystemConnector connector;

	public Triples(FileSystemConnector connector) {
		this.connector = connector;
	}

	public void fill(Index index, TripleFilter filter) throws Exception {
		NxParser triples = new NxParser(connector.content());
		while(triples.hasNext()){
			try{
				NTriple triple = new NTriple(triples.next());
				if(filter.matches(triple)) index.add(triple);
			}
			catch(Exception e){}
		}
	}
}
