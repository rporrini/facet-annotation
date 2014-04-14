package it.disco.unimib.labeller.index;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.semanticweb.yars.nx.parser.NxParser;


public class Triples {

	private static final Pattern isAcceptable = Pattern.compile("^<.+> <.+> (?<object>.+) \\.");
	private FileSystemConnector connector;

	public Triples(FileSystemConnector connector) {
		this.connector = connector;
	}

	public void fill(Index index, TripleFilter filter) throws Exception {
		LineIterator lines = IOUtils.lineIterator(connector.content(), "UTF-8");
		while(lines.hasNext()){
			String line = lines.nextLine();
			Matcher matcher = isAcceptable.matcher(line);
			if(!matcher.matches()) continue;
			String rawObject = matcher.group("object");
			line = line.replace(rawObject, rawObject.replace(" ", "%20"));
			try{
				NTriple triple = new NTriple(NxParser.parseNodes(line));
				if(filter.matches(triple)) index.add(triple);
			}catch(Exception e){}
		}
	}
}
