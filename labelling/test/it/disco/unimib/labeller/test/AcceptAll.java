package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.index.NTriple;
import it.disco.unimib.labeller.index.TripleFilter;

public class AcceptAll implements TripleFilter{

	@Override
	public boolean matches(NTriple triple) {
		return true;
	}
	
}