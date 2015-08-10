package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CandidateProperty;

import java.util.List;

public interface Summary {

	public String result();

	public Summary track(GoldStandardFacet group, List<CandidateProperty> results) throws Exception;
}