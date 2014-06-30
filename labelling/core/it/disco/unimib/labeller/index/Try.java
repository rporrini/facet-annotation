package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.io.File;
import java.util.List;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class Try {

	public static void main(String[] args) throws Exception {
		Score score = new WeightedPredicates(new SimilarityMetricWrapper(new JaccardSimilarity()));
		FullTextQuery query = new MandatoryContext();
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood")
			.predicateAnnotationWithCustomGrouping(score, query, "../evaluation/labeller-indexes/dbpedia/properties", "dbpedia").getAlgorithm();
		
		GoldStandardGroup group = new UnorderedGroups(new File("../evaluation/gold-standard-enhanced/")).getGroupById(1011013747);
		
		System.out.println(group.context());
		List<AnnotationResult> results = maximumLikelihood.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}
}
