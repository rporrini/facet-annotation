package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CandidateResource;

import java.util.List;

public interface Summary {

	public String result();

	public Summary track(GoldStandardFacet group, List<CandidateResource> results) throws Exception;
}