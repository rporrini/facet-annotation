package it.disco.unimib.labeller.index;


public class NoContext implements TripleSelectionCriterion{
	
	private AllValues allValues;

	public NoContext(AllValues constraints) {
		allValues = constraints;
	}

	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception {
		return allValues.createQuery(values.first(), literalField);
	}
}