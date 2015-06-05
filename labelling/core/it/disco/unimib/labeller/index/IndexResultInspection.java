package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;

public class IndexResultInspection implements Index{

	private Index index;

	public IndexResultInspection(Index indexToInspect) {
		this.index = indexToInspect;
	}
	
	@Override
	public long count(Constraint query) throws Exception {
		return index.count(query);
	}

	@Override
	public CandidateResourceSet get(ContextualizedValues request, Constraint query) throws Exception {
		CandidateResourceSet candidates = index.get(request, query);
		new Events().debug("domain: " + request.domain() + " - value: " + request.first());
		return candidates;
	}
	
}