package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public interface SingleFieldSelectionCriterion {

	public IndexQuery createQuery(String value, String field, Analyzer analyzer) throws Exception;

}