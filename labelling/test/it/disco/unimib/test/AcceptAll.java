package it.disco.unimib.test;

import it.disco.unimib.index.TripleFilter;

import com.hp.hpl.jena.graph.Triple;

public class AcceptAll implements TripleFilter{

	@Override
	public boolean matches(Triple triple) {
		return true;
	}
	
}