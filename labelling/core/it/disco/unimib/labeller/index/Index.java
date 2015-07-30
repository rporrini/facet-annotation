package it.disco.unimib.labeller.index;

public interface Index {
	
	public long count(Constraint query) throws Exception;

	public CandidateResources get(ContextualizedValues request, Constraint query) throws Exception;
}