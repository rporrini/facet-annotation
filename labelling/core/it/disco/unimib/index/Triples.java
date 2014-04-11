package it.disco.unimib.index;

import org.apache.jena.riot.RiotReader;
import org.apache.jena.riot.lang.LangNTriples;
import org.apache.jena.riot.tokens.TokenizerFactory;

import com.hp.hpl.jena.graph.Triple;


public class Triples {

	private FileSystemConnector connector;

	public Triples(FileSystemConnector connector) {
		this.connector = connector;
	}

	public void fill(Index index, TripleFilter filter) throws Exception {
		LangNTriples triples = RiotReader.createParserNTriples(TokenizerFactory.makeTokenizerUTF8(connector.content()), null);
		while(triples.hasNext()){
			Triple triple = triples.next();
			if(filter.matches(triple)) index.add(triple);
		}
		index.close();
	}
}
