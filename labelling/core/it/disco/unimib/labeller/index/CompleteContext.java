package it.disco.unimib.labeller.index;



public class CompleteContext implements TripleSelectionCriterion{

	private AllValues allValues;

	public CompleteContext(AllValues constraints) {
		allValues = constraints;
	}

	@Override
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception {
		return allValues.createQuery(value, literalField).all().match(context, contextField);
	}
}