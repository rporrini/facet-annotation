package it.disco.unimib.index;

import org.apache.jena.riot.RiotReader;
import org.apache.jena.riot.lang.LangNTriples;
import org.apache.jena.riot.tokens.TokenizerFactory;


public class Triples {

	private FileSystemConnector connector;

	public Triples(FileSystemConnector connector) {
		this.connector = connector;
	}

	public void fill(Index index) throws Exception {
		LangNTriples triples = RiotReader.createParserNTriples(TokenizerFactory.makeTokenizerUTF8(connector.content()), null);
		while(triples.hasNext()){
			index.add(triples.next());
		}
		index.close();
	}
}
