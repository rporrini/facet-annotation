package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.util.Arrays;
import java.util.List;




public class Try {

	public static void main(String[] args) throws Exception {
		RankingStrategy ranking = new RankByJaccard();
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood").predicateAnnotation(new RankInspection(ranking)).getAlgorithm();
		List<AnnotationResult> results = maximumLikelihood.typeOf("television shows", Arrays.asList(new String[]{
								"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }
		));	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}

}
