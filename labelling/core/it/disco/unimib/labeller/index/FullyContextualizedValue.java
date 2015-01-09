package it.disco.unimib.labeller.index;

public class FullyContextualizedValue implements SelectionCriterion{

	private IndexFields fields;

	public FullyContextualizedValue(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return fields.toConstraint()
					.all()
					.match(values.first(), fields.literal())
					.match(values.domain(), fields.context());
	}
}