package it.disco.unimib.labeller.index;

public class OnlyProperty implements SelectionCriterion{

	private IndexFields fields;

	public OnlyProperty(IndexFields fields) {
		this.fields = fields;
	}
	
	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint()
						.matchExactly(values.first(), fields.propertyId());
	}
}