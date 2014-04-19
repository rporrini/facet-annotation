package it.disco.unimib.labeller.index;


public class AcceptAll implements TripleFilter{

	@Override
	public boolean matches(NTriple triple) {
		return true;
	}
	
}