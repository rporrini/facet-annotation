package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class PartialContext implements TripleSelectionCriterion {

	private SingleFieldSelectionCriterion criterion;

	public PartialContext(SingleFieldSelectionCriterion criterion) {
		this.criterion = criterion;
	}

	@Override
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		return criterion.createQuery(value, literalField, analyzer).any().match(context, contextField);
	}
}
