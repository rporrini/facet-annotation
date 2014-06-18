package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.io.File;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class Try {

	public static void main(String[] args) throws Exception {
		//String file = "allstartnba_basketball players_draft year_players-by-draft-pick.htm"
		String file = "wikipedia_movies_producer_AFI's_100_Years...100_Movies_(10th_Anniversary_Edition)";
		GoldStandardGroup group = new GoldStandardGroup(new FileSystemConnector(new File("../evaluation/gold-standard-enhanced/" + file)));
		
		Score score = new WeightedPredicates(new JaccardSimilarity());
		FullTextQuery query = new OptionalContext();
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood").predicateAnnotationWithCustomGrouping(score, query, "../evaluation/labeller-indexes/dbpedia/properties", "dbpedia").getAlgorithm();
		
		System.out.println(group.context());
		
		List<AnnotationResult> results = maximumLikelihood.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}
}
