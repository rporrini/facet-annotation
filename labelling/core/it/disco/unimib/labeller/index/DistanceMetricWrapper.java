package it.disco.unimib.labeller.index;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;

public class DistanceMetricWrapper implements SimilarityMetric{
	private AbstractStringMetric metric;

	public DistanceMetricWrapper(AbstractStringMetric metric){
		this.metric = metric;
	}
	
	@Override
	public float getSimilarity(String s1, String s2) {
		return 1.0f - metric.getSimilarity(s1, s2);
	}
}