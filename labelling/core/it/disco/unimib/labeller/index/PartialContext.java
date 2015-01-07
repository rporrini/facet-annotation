package it.disco.unimib.labeller.index;


public class PartialContext implements TripleSelectionCriterion {

	private SingleFieldSelectionCriterion criterion;

	public PartialContext(SingleFieldSelectionCriterion criterion) {
		this.criterion = criterion;
	}

	@Override
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception {
		return criterion.createQuery(value, literalField).any().match(context, contextField);
	}
}
