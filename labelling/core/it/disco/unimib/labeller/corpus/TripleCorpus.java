package it.disco.unimib.labeller.corpus;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.NTriple;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.index.TripleStore;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class TripleCorpus implements TripleStore{

	private OutputFile file;
	private TripleIndex labels;
	private TripleIndex types;

	public TripleCorpus(TripleIndex types, TripleIndex labels, OutputFile file) {
		this.file = file;
		this.types = types;
		this.labels = labels;
	}

	@Override
	public TripleCorpus add(NTriple triple) throws Exception {
		List<String> values = getLabels(triple.object());
		List<String> types = new ArrayList<String>();
		for(CandidatePredicate type : this.types.get(triple.subject(), "any")){
			types.addAll(getLabels(type.value()));
		}
		for(String type : types){
			for(String value : values){
				file.write(type + " " + triple.predicate().uri() + " " + value);
			}
		}
		return this;
	}

	private List<String> getLabels(String uri) throws Exception {
		List<String> values = new ArrayList<String>();
		if(!uri.contains("http://")){
			values.add(uri);
		}
		else{
			for(CandidatePredicate label : this.labels.get(uri, "any")){
				values.add(label.value());
			}
		}
		List<String> result = new ArrayList<String>();
		for(String value : values){
			result.add(tokenize(new EnglishAnalyzer(Version.LUCENE_45), value));
		}
		return result;
	}
	
	private String tokenize(Analyzer analyzer, String string) throws Exception {
		List<String> result = new ArrayList<String>();
		TokenStream stream = analyzer.tokenStream(null, new StringReader(string));
		stream.reset();
		while (stream.incrementToken()) {
			result.add(stream.getAttribute(CharTermAttribute.class).toString());
		}
		analyzer.close();
		return StringUtils.join(result, " ");
	}
}
