package it.disco.unimib.labeller.properties;

public class WeightedJaccardSimilarity {
	
	public double between(TypeDistribution d1, TypeDistribution d2){
		double intersection = 0.0;
		double union = 0.0;
		
		for(String t1 : d1.all()){
			double w1 = weightOf(t1, d1);
			double w2 = weightOf(t1, d2);
			
			intersection += Math.min(w1, w2);
			union += Math.max(w1, w2);
		}
		
		return safeDivision(intersection, union);
	}
	
	private double weightOf(String type, TypeDistribution distribution) {
		return safeDivision(distribution.propertyOccurrenceForType(type), distribution.typeOccurrence(type));
	}
	
	private double safeDivision(double dividendum, double divisor) {
		if(divisor == 0) return 0;
		return dividendum / divisor;
	}
}