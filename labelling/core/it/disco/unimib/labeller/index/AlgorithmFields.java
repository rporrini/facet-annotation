package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.util.Version;

class AlgorithmFields{
	
	public Analyzer analyzer() {
		Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();
		analyzers.put(property(), new KeywordAnalyzer());
		analyzers.put(namespace(), new KeywordAnalyzer());
		analyzers.put(label(), new KeywordAnalyzer());
		return new PerFieldAnalyzerWrapper(new EnglishAnalyzer(Version.LUCENE_45), analyzers);
	}
	
	public String label(){
		return "label";
	}
	
	public String namespace(){
		return "namespace";
	}
	
	public String literal() {
		return "literal";
	}
	
	public String property() {
		return "property";
	}
	
	public String context() {
		return "context";
	}
}