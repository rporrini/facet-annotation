package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.UnorderedFacets;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.SimilarityMetric;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.LogarithmicPredicateSpecificy;
import it.disco.unimib.labeller.predicates.TopK;
import it.disco.unimib.labeller.predicates.WeightedFrequencyCoverageAndSpecificity;

import java.io.File;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class RunAlgorithm {

	public static void main(String[] args) throws Exception {
		String knowledgeBase = "dbpedia";
		NIOFSDirectory indexDirectory = new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + knowledgeBase + "/properties"));
		SimilarityMetric occurrences = new SimilarityMetricWrapper(new JaccardSimilarity());
		ContextualizedEvidence index = new ContextualizedEvidence(indexDirectory, occurrences, new IndexFields(knowledgeBase));
		PartialContext valueMatching = new PartialContext(new AllValues());
		LogarithmicPredicateSpecificy predicateSpecificity = new LogarithmicPredicateSpecificy(index);
		WeightedFrequencyCoverageAndSpecificity majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, valueMatching, predicateSpecificity);
		
		UnorderedFacets groups = new UnorderedFacets(new File("../evaluation/gold-standards/dbpedia-enhanced/"));
		for(int id : ids()){
			annotate(majorityHitWeighted, groups, id);
		}
	}

	private static int[] ids() {
		return new int[]{
//				1668711967,
//				1689442184,
				1744816435,
//				1802054300,
//				2021450258,
//				2125380335,
//				753388668
		};
	}
	
	private static void annotate(AnnotationAlgorithm algorithm, UnorderedFacets groups, int id) throws Exception {
		GoldStandardFacet group = groups.getGroupById(id);
		System.out.println(group.context() + " " + id);
		List<CandidateResource> results = new TopK(500, algorithm).typeOf(group.context(), group.elements());	
		for(CandidateResource result : results){
			System.out.println(result);
		}
	}
}
