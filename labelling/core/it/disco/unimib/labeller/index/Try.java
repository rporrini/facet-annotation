package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.io.File;
import java.util.List;




public class Try {

	public static void main(String[] args) throws Exception {
		String file = "tv series_wikipedia_television shows_director_List_of_How_I_Met_Your_Mother_episodes";
		GoldStandardGroup group = new GoldStandardGroup(new FileSystemConnector(new File("../evaluation/gold-standard/" + file)));
		
		RankingStrategy ranking = new RankByJaccard();
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood").predicateAnnotation(new RankInspection(ranking)).getAlgorithm();
		
		System.out.println(group.context());
		
		List<AnnotationResult> results = maximumLikelihood.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}

}
