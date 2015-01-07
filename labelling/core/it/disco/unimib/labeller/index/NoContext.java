package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class NoContext implements TripleSelectionCriterion{
	
	private AllValues allValues;

	public NoContext(AllValues constraints) {
		allValues = constraints;
	}

	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		return allValues.createQuery(value, literalField, analyzer);
	}
}