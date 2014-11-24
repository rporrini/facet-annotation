package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.Triples;
import it.disco.unimib.labeller.unit.TemporaryDirectory;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.lucene.store.NIOFSDirectory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 100, warmupRounds = 1, concurrency=2)
public class EntityValuesPerformance extends AbstractBenchmark{

	private static TemporaryDirectory indexDirectory;
	private static EntityValues types;
	private static Random generator;
	
	private static List<String> entities;
	
	@BeforeClass
	public static void setUp() throws Exception{
		String here = "test/it/disco/unimib/labeller/performance/";
		
		indexDirectory = new TemporaryDirectory();
		
		types = new EntityValues(new NIOFSDirectory(indexDirectory.get()));
		new Triples(new InputFile(new File(here + "categories.nt"))).fill(types, new AcceptAll());
		types.closeWriter();
		
		entities = new InputFile(new File("test/it/disco/unimib/labeller/performance/" + "queries.txt")).lines();
		generator = new Random();
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		indexDirectory.delete();
	}
	
	@Test
	public void queryResponse() throws Exception {
		for(int i=0; i < 50; i++){
			types.get(pickRandomEntity());
		}
	}
	
	private String pickRandomEntity(){
		return entities.get(generator.nextInt(entities.size()));
	}
}
