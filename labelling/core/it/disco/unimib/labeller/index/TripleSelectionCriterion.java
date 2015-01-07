package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public interface TripleSelectionCriterion{
	
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception;
}