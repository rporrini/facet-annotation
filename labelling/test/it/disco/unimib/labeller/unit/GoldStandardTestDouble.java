package it.disco.unimib.labeller.unit;

import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;

import java.util.ArrayList;
import java.util.List;

public class GoldStandardTestDouble implements GoldStandard{

	List<GoldStandardFacet> groups = new ArrayList<GoldStandardFacet>();
	
	@Override
	public GoldStandardFacet[] getFacets() {
		return groups.toArray(new GoldStandardFacet[groups.size()]);
	}
	
	public GoldStandardTestDouble withGroup(String name){
		groups.add(new GoldStandardFacet(new InputFileTestDouble().withName(name)));
		return this;
	}

	@Override
	public GoldStandardFacet getGroupById(int id) {
		return null;
	}
}