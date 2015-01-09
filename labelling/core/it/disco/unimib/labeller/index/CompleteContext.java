package it.disco.unimib.labeller.index;

public class CompleteContext implements TripleSelectionCriterion{

	private IndexFields fields;

	public CompleteContext(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return new AllValues(fields)
					.createQuery(values.first(), fields.literal())
					.all()
					.match(values.domain(), fields.context());
	}
}