package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.UnorderedGroups;
import it.disco.unimib.labeller.labelling.AnnotationAlgorithm;
import it.disco.unimib.labeller.labelling.MajorityHitWeighted;
import it.disco.unimib.labeller.labelling.PredicateAndContextWeight;
import it.disco.unimib.labeller.labelling.PredicateWithoutWeight;

import java.io.File;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class Try {

	public static void main(String[] args) throws Exception {
		String knowledgeBase = "dbpedia";
		
		NIOFSDirectory indexDirectory = new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + knowledgeBase + "/properties"));
		GroupBySearch index = new GroupBySearch(indexDirectory, new WeightedPredicates(new SimilarityMetricWrapper(new JaccardSimilarity())), new KnowledgeBase(knowledgeBase));
		MajorityHitWeighted majorityHitWeighted = new MajorityHitWeighted(index, new PredicateAndContextWeight(index), new PredicateWithoutWeight());
		
		UnorderedGroups groups = new UnorderedGroups(new File("../evaluation/gold-standard-enhanced/"));
		for(int id : ids(knowledgeBase)){
			annotate(majorityHitWeighted, groups, id);
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
	
	private static void annotate(AnnotationAlgorithm algorithm, UnorderedGroups groups, int id) throws Exception {
		GoldStandardGroup group = groups.getGroupById(id);
		System.out.println(group.context() + " " + id);
		List<AnnotationResult> results = algorithm.typeOf(group.context(), group.elements());	
		for(AnnotationResult result : results){
			System.out.println(result);
		}
	}
}
