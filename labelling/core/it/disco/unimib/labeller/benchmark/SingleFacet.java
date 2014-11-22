package it.disco.unimib.labeller.benchmark;


public class SingleFacet implements GoldStandard {

	private GoldStandard goldStandard;
	private int id;

	public SingleFacet(GoldStandard goldStandard, int id) {
		this.goldStandard = goldStandard;
		this.id = id;
	}

	@Override
	public GoldStandardGroup[] getGroups() {
		return new GoldStandardGroup[]{ getGroupById(id)};
	}

	@Override
	public GoldStandardGroup getGroupById(int id) {
		return goldStandard.getGroupById(id);
	}
}
