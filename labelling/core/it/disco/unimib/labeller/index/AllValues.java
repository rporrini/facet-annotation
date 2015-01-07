package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;

public class AllValues implements SingleFieldSelectionCriterion {

	public BooleanQuery createQuery(String value, String field, Analyzer analyzer) throws Exception {
		IndexQuery query = new IndexQuery(analyzer).all().match(value, field);
		return query.build();
	}
}
