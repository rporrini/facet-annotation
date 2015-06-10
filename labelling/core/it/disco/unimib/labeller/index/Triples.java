package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.semanticweb.yars.nx.parser.NxParser;


public class Triples {

	private static final Pattern isAcceptable = Pattern.compile("^<.+> <.+> (?<object>.+) \\.");
	private InputFile connector;

	public Triples(InputFile connector) {
		this.connector = connector;
	}

	public void fill(WriteStore index, TripleFilter filter) throws Exception {
		LineIterator lines = IOUtils.lineIterator(connector.content(), "UTF-8");
		int processedLines = 0;
		int skippedLines = 0;
		while(lines.hasNext()){
			if(processedLines % 100000 == 0){
				Events.verbose().debug("processed " + processedLines + " lines of file " + connector.name() + " (" + skippedLines + " skipped)");
			}
			processedLines++;
			String line = lines.nextLine();
			Matcher matcher = isAcceptable.matcher(line);
			if(!matcher.matches()){
				skippedLines++;
				continue;
			}
			String rawObject = matcher.group("object");
			if(rawObject.startsWith("<")) line = line.replace(rawObject, rawObject.replace(" ", "%20"));
			try{
				NTriple triple = new NTriple(NxParser.parseNodes(line));
				if(filter.matches(triple)) index.add(triple);
				else skippedLines++;
			}catch(Exception e){
				Events.verbose().error("error processing " + connector.name(), e);
			}
		}
	}
}
