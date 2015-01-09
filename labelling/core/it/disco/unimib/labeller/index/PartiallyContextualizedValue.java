package it.disco.unimib.labeller.index;


public class PartiallyContextualizedValue implements SelectionCriterion {

	private IndexFields fields;

	public PartiallyContextualizedValue(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint()
						.all()
						.match(values.first(), fields.literal())
						.any()
						.match(values.domain(), fields.context());
	}
}
