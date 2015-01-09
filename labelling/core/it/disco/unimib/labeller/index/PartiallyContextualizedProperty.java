package it.disco.unimib.labeller.index;

public class PartiallyContextualizedProperty implements SelectionCriterion {

	private IndexFields fields;

	public PartiallyContextualizedProperty(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint()
					.matchExactly(values.first(), fields.propertyId())
					.any()
					.match(values.domain(), fields.context());
	}
}