package it.disco.unimib.labeller.index;


public class PartialContext implements TripleSelectionCriterion {

	private SingleFieldSelectionCriterion criterion;

	public PartialContext(SingleFieldSelectionCriterion criterion) {
		this.criterion = criterion;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception {
		return criterion.createQuery(values.first(), literalField).any().match(values.domain(), contextField);
	}
}
