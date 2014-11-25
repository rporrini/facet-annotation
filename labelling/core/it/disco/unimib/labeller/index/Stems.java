package it.disco.unimib.labeller.index;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Stems{
	
	private IndexFields fields;

	public Stems(IndexFields fields){
		this.fields = fields;
	}
	
	public String of(String toStem) throws IOException {
		String stemmedContext = "";
		TokenStream stream = fields.analyzer().tokenStream("any", new StringReader(toStem));
		CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while(stream.incrementToken()) {
            stemmedContext = stemmedContext + " " + termAtt.toString();
        }
		return stemmedContext.trim();
	}
}