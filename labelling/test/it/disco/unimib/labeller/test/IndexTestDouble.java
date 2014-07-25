package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexTestDouble implements Index{

	private HashMap<String, List<AnnotationResult>> results = new HashMap<String, List<AnnotationResult>>();
	
	@Override
	public List<AnnotationResult> get(String value, String context, FullTextQuery query) throws Exception {
		List<AnnotationResult> result = results.get(value);
		if(result == null) result = new ArrayList<AnnotationResult>();
		return result;
	}

	@Override
	public long count(String predicate, String context, FullTextQuery query) throws Exception {
		return 0;
	}
	
	public IndexTestDouble resultFor(String value, String predicate, double score){
		if(!results.containsKey(value)) results.put(value, new ArrayList<AnnotationResult>());
		results.get(value).add(new AnnotationResult(predicate, score));
		return this;
	}
}