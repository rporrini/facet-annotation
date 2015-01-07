package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;

public interface SingleFieldSelectionCriterion {

	public BooleanQuery createQuery(String value, String field, Analyzer analyzer) throws Exception;

}