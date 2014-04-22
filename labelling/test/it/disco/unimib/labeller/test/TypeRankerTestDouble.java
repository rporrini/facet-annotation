package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.TypeRanker;

import java.net.URISyntaxException;

public class TypeRankerTestDouble implements TypeRanker{

	String type;
	
	@Override
	public AnnotationResult typeOf(String... entity) throws URISyntaxException {
		return new AnnotationResult(this.type, 1);
	}

	public TypeRankerTestDouble thatReturns(String type) {
		this.type = type;
		return this;
	}
}