package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.CandidatePredicate;

import java.util.List;

public interface Summary {

	public String result();

	public Summary track(GoldStandardGroup group, List<CandidatePredicate> results) throws Exception;
}