package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.BenchmarkConfiguration;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;

import java.io.File;
import java.util.List;

public class Try {

	public static void main(String[] args) throws Exception {
//		Score score = new WeightedPredicates(new SimilarityMetricWrapper(new JaccardSimilarity()));
//		Score score = new CountPredicates();
		FullTextQuery query = new MandatoryContext();
		String knowledgeBase = "dbpedia";
		AnnotationAlgorithm maximumLikelihood = new BenchmarkConfiguration("maximum likelihood")
			.majorityAnnotation(0.1, query, "../evaluation/labeller-indexes/" + knowledgeBase + "/properties", new KnowledgeBase(knowledgeBase)).getAlgorithm();
//			.predicateAnnotationWithCustomGrouping(score, query, "../evaluation/labeller-indexes/" + knowledgeBase + "/properties", knowledgeBase).getAlgorithm();
		
		UnorderedGroups groups = new UnorderedGroups(new File("../evaluation/gold-standard-enhanced/"));
		
		for(int id : ids(knowledgeBase)){
			annotate(maximumLikelihood, groups, id);
		}
	}

	private static int[] ids(String knowledgeBase) {
		if(knowledgeBase.equals("dbpedia"))
			return new int[]{
				702159889,
				268043830,
				213755943,
				2125380335,
				2117679317,
				1796458291,
				1744816435,
				1689442184,
				148489175,
				1161561471,
				1088443226,
				1011013747
		};
		else
			return new int[]{
				1710161454,
				1710161453,
				78397110,
				1923976096,
				1923976121,
				1975319113,
				1101244520,
				1101244644,
				1101244490,
				1101244607,
				1101244518,
				1101244615,
				1143742855,
				609086460,
				423159788,
				423159641
		};
	}
	
	private static void annotate(AnnotationAlgorithm maximumLikelihood, UnorderedGroups groups, int id) throws Exception {
		GoldStandardGroup group = groups.getGroupById(id);
		System.out.println(group.context() + " " + id);
		List<AnnotationResult> results = maximumLikelihood.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}
}
