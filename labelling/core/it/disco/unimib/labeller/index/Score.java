package it.disco.unimib.labeller.index;

import java.util.List;

public interface Score {

	public void accumulate(String label, String context);

	public List<AnnotationResult> toResults();

	public void clear();

}