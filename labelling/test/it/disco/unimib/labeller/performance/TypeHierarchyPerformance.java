package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.TypeHierarchy;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 10, warmupRounds = 0, concurrency=2)
public class TypeHierarchyPerformance extends AbstractBenchmark{

	private static TypeHierarchy hierarchy;

	@BeforeClass
	public static void annotateAnotherFacetInOrderToEnsureThatLuceneDoesNotCacheResults() throws Exception{
		hierarchy = new TypeHierarchy(
										new InputFile(new File("../evaluation/dbpedia-type-tree/type-tree.nt")),
										new InputFile(new File("../evaluation/dbpedia-category-tree/category-tree.nt")));
	}
	
	@Test
	public void scaledDepthOfAType() {
		hierarchy.typeOf("http://dbpedia.org/ontology/MusicalArtist").scaledDepth();
	}
	
	@Test
	public void scaledDepthOfACategory() {
		hierarchy.typeOf("http://dbpedia.org/resource/Category:World_War_II").scaledDepth();
	}
}
