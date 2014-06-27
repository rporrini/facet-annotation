package it.disco.unimib.labeller.index;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;

public class SimilarityMetricWrapper implements SimilarityMetric{

	private AbstractStringMetric metric;

	public SimilarityMetricWrapper(AbstractStringMetric metric){
		this.metric = metric;
	}
	
	@Override
	public float getSimilarity(String s1, String s2) {
		return metric.getSimilarity(s1, s2);
	}
}