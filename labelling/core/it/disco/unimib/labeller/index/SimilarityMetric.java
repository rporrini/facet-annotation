package it.disco.unimib.labeller.index;

public interface SimilarityMetric{
	
	public float getSimilarity(String s1, String s2);
}