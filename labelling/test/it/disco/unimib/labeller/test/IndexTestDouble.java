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
	public List<AnnotationResult> get(String type, String context, FullTextQuery query) throws Exception {
		List<AnnotationResult> result = results.get(type);
		if(result == null) result = new ArrayList<AnnotationResult>();
		return result;
	}
	
	public IndexTestDouble resultFor(String type, String predicate, double score){
		if(!results.containsKey(type)) results.put(type, new ArrayList<AnnotationResult>());
		results.get(type).add(new AnnotationResult(predicate, score));
		return this;
	}
}