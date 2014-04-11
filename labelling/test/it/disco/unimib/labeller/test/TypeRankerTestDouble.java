package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.labelling.TypeRanker;

import java.net.URISyntaxException;

public class TypeRankerTestDouble implements TypeRanker{

	String type;
	
	@Override
	public String typeOf(String... entity) throws URISyntaxException {
		return this.type;
	}

	public TypeRankerTestDouble thatReturns(String type) {
		this.type = type;
		return this;
	}
}