package it.disco.unimib.labeller.index;


public class PartialContext implements TripleSelectionCriterion {

	private IndexFields fields;

	public PartialContext(IndexFields fields) {
		this.fields = fields;
	}

	@Override
	public Constraint asQuery(ContextualizedValues values, String literalField) throws Exception {
		return new AllValues(fields)
						.createQuery(values.first(), literalField)
						.any()
						.match(values.domain(), fields.context());
	}
}
