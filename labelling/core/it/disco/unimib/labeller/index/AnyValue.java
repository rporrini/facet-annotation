package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class AnyValue implements SingleFieldSelectionCriterion{
	
	public IndexQuery createQuery(String value, String field, Analyzer analyzer) throws Exception {
		IndexQuery query = new IndexQuery(analyzer).any().match(value, field);
		return query;
	}
}