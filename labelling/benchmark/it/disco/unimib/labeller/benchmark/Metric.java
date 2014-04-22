package it.disco.unimib.labeller.benchmark;

import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.List;

public interface Metric {

	public String result();

	public Metric track(GoldStandardGroup group, List<AnnotationResult> results);
}