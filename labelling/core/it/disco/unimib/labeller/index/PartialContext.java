package it.disco.unimib.labeller.index;


public class PartialContext implements TripleSelectionCriterion {

	private IndexFields fields;

	public PartialContext(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values) throws Exception {
		return new AllValues(fields)
						.createQuery(values.first(), fields.literal())
						.any()
						.match(values.domain(), fields.context());
	}
}
