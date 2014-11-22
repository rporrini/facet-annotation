package it.disco.unimib.labeller.index;

import java.util.List;

public interface Occurrences {

	public void accumulate(String label, String context, String targetContext);

	public List<CandidateResource> toResults();

	public void clear();

}