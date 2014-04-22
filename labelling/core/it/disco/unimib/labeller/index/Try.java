package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.util.Arrays;
import java.util.List;

public class Try {

	public static void main(String[] args) throws Exception {
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood").predicateAnnotation().getAlgorithm();
		List<AnnotationResult> results = maximumLikelihood.typeOf("\"how i met your mother\"", Arrays.asList(new String[]{
								"Michael Shea", 
								"Neil Patrick Harris", 
								"Pamela Fryman", 
								"Rob Greenberg"}
		));	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}

}
