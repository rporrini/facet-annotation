package it.disco.unimib.labeller.index;


public class OnlyValue implements SelectionCriterion{
	
	private IndexFields fields;

	public OnlyValue(IndexFields fields) {
		this.fields = fields;
	}

	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint().all().match(values.first(), fields.literal());
	}
}