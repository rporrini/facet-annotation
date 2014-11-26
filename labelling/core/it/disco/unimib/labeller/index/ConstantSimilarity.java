package it.disco.unimib.labeller.index;

public class ConstantSimilarity implements SimilarityMetric {

	public float getSimilarity(String string, String otherString) {
		return 1.0f;
	}
}
