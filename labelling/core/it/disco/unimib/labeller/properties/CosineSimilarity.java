package it.disco.unimib.labeller.properties;

public class CosineSimilarity {

	public double between(TypeDistribution d1, TypeDistribution d2) {
		
		return safeDivision(dotProduct(d1, d2), magnitudeOf(d1) * magnitudeOf(d2));
	}

	private double dotProduct(TypeDistribution d1, TypeDistribution d2){
		double result = 0;
		for(String t1 : d1.all()){
			result += weightOf(t1, d1) * weightOf(t1, d2);
		}
		return result;
	}
	
	private double magnitudeOf(TypeDistribution distribution){
		double result = 0;
		for(String type : distribution.all()){
			double weight = weightOf(type, distribution);
			result += Math.pow(weight, 2);
		}
		return Math.sqrt(result);
	}

	private double weightOf(String type, TypeDistribution distribution) {
		return safeDivision(distribution.propertyOccurrenceForType(type), distribution.typeOccurrence(type));
	}
	
	private double safeDivision(double dividendum, double divisor) {
		if(divisor == 0) return 0;
		return dividendum / divisor;
	}
}
