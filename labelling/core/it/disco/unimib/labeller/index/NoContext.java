package it.disco.unimib.labeller.index;


public class NoContext implements TripleSelectionCriterion{
	
	private IndexFields fields;

	public NoContext(IndexFields fields) {
		this.fields = fields;
	}

	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return new AllValues(fields)
					.createQuery(values.first(), fields.literal());
	}
}