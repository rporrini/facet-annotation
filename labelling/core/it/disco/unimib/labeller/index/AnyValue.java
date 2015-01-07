package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class AnyValue implements SingleFieldSelectionCriterion{
	
	private Analyzer analyzer;

	public AnyValue(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public IndexQuery createQuery(String value, String field) throws Exception {
		IndexQuery query = new IndexQuery(this.analyzer).any().match(value, field);
		return query;
	}
}