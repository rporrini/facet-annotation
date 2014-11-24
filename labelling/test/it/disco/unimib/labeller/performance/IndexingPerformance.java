package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.Triples;
import it.disco.unimib.labeller.unit.TemporaryDirectory;

import java.io.File;

import org.apache.lucene.store.NIOFSDirectory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 4, warmupRounds = 0, concurrency=2)
public class IndexingPerformance extends AbstractBenchmark{

	private static TemporaryDirectory indexDirectory;
	private static Evidence predicates;
	private static EntityValues types;
	private static EntityValues labels;

	@BeforeClass
	public static void setUp() throws Exception{
		indexDirectory = new TemporaryDirectory();
		types = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/types")));
		labels = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/labels")));
		
		predicates = new Evidence(new NIOFSDirectory(indexDirectory.get()), 
											types, 
											labels,
											null, 
											new NoContext(new AllValues()),
											new IndexFields("dbpedia"));
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		predicates.closeWriter();
		indexDirectory.delete();
	}
	
	@Test
	public void indexSampleFile() throws Exception {
		
		new Triples(new InputFile(new File("test/it/disco/unimib/labeller/performance/properties.nt"))).fill(predicates, new AcceptAll());
	}
}
