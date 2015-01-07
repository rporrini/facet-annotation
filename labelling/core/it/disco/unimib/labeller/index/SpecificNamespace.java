package it.disco.unimib.labeller.index;


public class SpecificNamespace implements TripleSelectionCriterion {

	private TripleSelectionCriterion query;
	private String namespace;

	public SpecificNamespace(String namespace, TripleSelectionCriterion query) {
		this.query = query;
		this.namespace = namespace;
	}

	@Override
	public IndexQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField) throws Exception {
		IndexQuery queryToDecorate = query.asQuery(value, context, literalField, contextField, namespaceField);
		return queryToDecorate.matchExactly(namespace, namespaceField);
	}

}
