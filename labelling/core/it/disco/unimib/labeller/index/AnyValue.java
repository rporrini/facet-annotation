package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;

public class AnyValue implements SingleFieldSelectionCriterion{
	
	public BooleanQuery createQuery(String value, String field, Analyzer analyzer) throws Exception {
		IndexQuery query = new IndexQuery(analyzer).any().match(value, field);
		return query.build();
	}
}