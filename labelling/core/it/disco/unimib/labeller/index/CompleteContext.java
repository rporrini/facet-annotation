package it.disco.unimib.labeller.index;

public class CompleteContext implements TripleSelectionCriterion{

	private IndexFields fields;

	public CompleteContext(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception {
		return new AllValues(fields).createQuery(values.first(), literalField).all().match(values.domain(), contextField);
	}
}