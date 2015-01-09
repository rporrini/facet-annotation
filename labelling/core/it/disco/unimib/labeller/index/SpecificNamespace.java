package it.disco.unimib.labeller.index;


public class SpecificNamespace implements TripleSelectionCriterion {

	private TripleSelectionCriterion query;
	private String namespace;

	public SpecificNamespace(String namespace, TripleSelectionCriterion query) {
		this.query = query;
		this.namespace = namespace;
	}

	@Override
	public Constraint asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception {
		return query
				.asQuery(value, context, literalField, contextField, namespaceField)
				.matchExactly(namespace, namespaceField);
	}

}
