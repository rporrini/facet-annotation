package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;

import java.util.ArrayList;
import java.util.List;

public class GoldStandardTestDouble implements GoldStandard{

	List<GoldStandardGroup> groups = new ArrayList<GoldStandardGroup>();
	
	@Override
	public GoldStandardGroup[] getGroups() {
		return groups.toArray(new GoldStandardGroup[groups.size()]);
	}
	
	public GoldStandardTestDouble withGroup(String name){
		groups.add(new GoldStandardGroup(new InputFileTestDouble().withName(name)));
		return this;
	}

	@Override
	public GoldStandardGroup getGroupById(int id) {
		return null;
	}
}