package it.disco.unimib.labeller.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;

public class IndexFields{
	
	private String knowledgeBase;

	public IndexFields(String knowledgeBase){
		this.knowledgeBase = knowledgeBase;
	}
	
	public Analyzer analyzer() {
		Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>();
		analyzers.put(property(), new KeywordAnalyzer());
		analyzers.put(namespace(), new KeywordAnalyzer());
		analyzers.put(label(), new KeywordAnalyzer());
		analyzers.put(objectType(), new KeywordAnalyzer());
		analyzers.put(subjectType(), new KeywordAnalyzer());
		return new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), analyzers);
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
	
	public String propertyId() {
		String field = label();
		if(knowledgeBase.equals("dbpedia")) field = property();
		return field;
	}
	
	public String context() {
		return "context";
	}	
	
	public String objectType(){
		return "object-type";
	}
	
	public String subjectType() {
		return "subjectType";
	}
	
	public Constraint toConstraint(){
		return new Constraint(analyzer());
	}
	
	public HashSet<String> fieldsToRead() {
		return new HashSet<String>(Arrays.asList(new String[]{
									propertyId(), 
									context(),
									subjectType(),
									objectType()}));
	}
	
	public Stems toStems() {
		return new Stems(analyzer());
	}
}