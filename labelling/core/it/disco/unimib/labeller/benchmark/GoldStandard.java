package it.disco.unimib.labeller.benchmark;

public interface GoldStandard {

	public GoldStandardFacet[] getFacets();

	GoldStandardFacet getGroupById(int id);
}