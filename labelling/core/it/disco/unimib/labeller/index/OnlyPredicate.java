package it.disco.unimib.labeller.index;

public class OnlyPredicate implements TripleSelectionCriterion{

	private IndexFields fields;

	public OnlyPredicate(IndexFields fields) {
		this.fields = fields;
	}
	
	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint().matchExactly(values.first(), fields.propertyId());
	}
}