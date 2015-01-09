package it.disco.unimib.labeller.index;

public class CompleteContext implements TripleSelectionCriterion{

	private AllValues allValues;

	public CompleteContext(AllValues constraints) {
		allValues = constraints;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception {
		return allValues.createQuery(values.first(), literalField).all().match(values.domain(), contextField);
	}
}