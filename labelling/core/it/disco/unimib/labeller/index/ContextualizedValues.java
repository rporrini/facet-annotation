package it.disco.unimib.labeller.index;

public class ContextualizedValues {
	
	private String domain;
	private String[] values;
	private String[] domainTypes;

	public ContextualizedValues(String domain, String[] values) {
		this.domain = domain;
		this.values = values;
	}
	
	public String domain(){
		return domain;
	}
	
	public String[] all(){
		return values;
	}

	public String first() {
		return values[0];
	}

	public ContextualizedValues[] split() {
		ContextualizedValues[] result = new ContextualizedValues[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = new ContextualizedValues(domain, new String[]{values[i]});
		}
		return result;
	}

	public void setDomains(String... types) {
		this.domainTypes = types;
	}

	public String[] domainTypes() {
		return domainTypes;
	}
}