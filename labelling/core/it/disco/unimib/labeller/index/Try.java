package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.io.File;
import java.util.List;

public class Try {

	public static void main(String[] args) throws Exception {
		String file = "allstartnba_basketball players_draft year_players-by-draft-pick.htm";
		GoldStandardGroup group = new GoldStandardGroup(new FileSystemConnector(new File("../evaluation/gold-standard-enhanced/" + file)));
		
		RankingStrategy ranking = new RankByFrequency();//new RankInspection(new RankByFrequency());
		FullTextQuery query = new MandatoryContext();//new SpecificNamespace("http://dbpedia.org/ontology/", new MandatoryContext());
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood").predicateAnnotation(ranking, query).getAlgorithm();
		
		System.out.println(group.context());
		
		List<AnnotationResult> results = maximumLikelihood.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}
}
