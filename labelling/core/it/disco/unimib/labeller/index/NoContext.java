package it.disco.unimib.labeller.index;


public class NoContext implements TripleSelectionCriterion{
	
	private IndexFields fields;

	public NoContext(IndexFields fields) {
		this.fields = fields;
	}

	public Constraint asQuery(ContextualizedValues values, String literalField, String contextField, String namespaceField) throws Exception {
		return new AllValues(fields).createQuery(values.first(), literalField);
	}
}