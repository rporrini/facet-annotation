package it.disco.unimib.labeller.corpus;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.NTriple;
import it.disco.unimib.labeller.index.WriteStore;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TripleCorpus implements WriteStore{

	private OutputFile file;
	private EntityValues labels;
	private EntityValues types;
	private Analyzer analyzer;

	public TripleCorpus(EntityValues types, EntityValues labels, OutputFile file, Analyzer analyzer) {
		this.file = file;
		this.types = types;
		this.labels = labels;
		this.analyzer = analyzer;
	}

	@Override
	public TripleCorpus add(NTriple triple) throws Exception {
		List<String> values = getLabels(triple.object().uri());
		List<String> types = new ArrayList<String>();
		for(CandidateProperty type : this.types.get(triple.subject().uri())){
			types.addAll(getLabels(type.uri()));
		}
		for(String type : types){
			for(String value : values){
				file.write(type + " " + triple.property().uri() + " " + value);
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
			for(CandidateProperty label : this.labels.get(uri)){
				values.add(label.uri());
			}
		}
		List<String> result = new ArrayList<String>();
		for(String value : values){
			result.add(tokenize(analyzer, value));
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
		stream.close();
		return StringUtils.join(result, " ");
	}
}
