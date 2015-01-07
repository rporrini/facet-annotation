package it.disco.unimib.labeller.index;


import org.apache.lucene.analysis.Analyzer;

public class CompleteContext implements TripleSelectionCriterion{

	private AllValues allValues;

	public CompleteContext(AllValues constraints) {
		allValues = constraints;
	}

	@Override
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		return allValues.createQuery(value, literalField, analyzer).all().match(context, contextField);
	}
}