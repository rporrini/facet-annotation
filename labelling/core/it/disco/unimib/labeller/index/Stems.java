package it.disco.unimib.labeller.index;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Stems{
	
	private Analyzer analyzer;

	public Stems(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public String of(String toStem) throws IOException {
		String stemmedContext = "";
		TokenStream stream = analyzer.tokenStream("any", new StringReader(toStem));
		CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
            stemmedContext = stemmedContext + " " + termAtt.toString();
        }
		stream.close();
		return stemmedContext.trim();
	}
}