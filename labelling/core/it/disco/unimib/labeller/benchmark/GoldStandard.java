package it.disco.unimib.labeller.benchmark;

public interface GoldStandard {

	public GoldStandardGroup[] getGroups();

	GoldStandardGroup getGroupById(int id);
}