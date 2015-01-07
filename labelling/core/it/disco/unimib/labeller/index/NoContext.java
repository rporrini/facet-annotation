package it.disco.unimib.labeller.index;


public class NoContext implements TripleSelectionCriterion{
	
	private AllValues allValues;

	public NoContext(AllValues constraints) {
		allValues = constraints;
	}

	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception {
		return allValues.createQuery(value, literalField);
	}
}