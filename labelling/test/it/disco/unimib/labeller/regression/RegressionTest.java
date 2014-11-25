package it.disco.unimib.labeller.regression;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.tools.TrecResultPredicate;

import java.util.List;

import org.junit.Test;

public class RegressionTest {

	@Test
	public void dbpediaExperiments() throws Exception {
		int movieDirectors = 1888395491;
		String knowledgeBase = "dbpedia";
		
		List<TrecResultPredicate> results = new CommandLineBenchmarkSimulation().run(knowledgeBase, movieDirectors);
		
		checkNonRegression(results, 
						   27,
						   "http://dbpedia.org/property/director",
						   "http://dbpedia.org/ontology/director",
						   "http://dbpedia.org/ontology/writer",
						   "http://dbpedia.org/property/writer");
	}
	
	@Test
	public void yagoExperiments() throws Exception {
		int wordnetState108654360 = 1091252161;
		String knowledgeBase = "yago1";
		
		List<TrecResultPredicate> results = new CommandLineBenchmarkSimulation().run(knowledgeBase, wordnetState108654360);
		
		checkNonRegression(results, 
						   8,
						   "hasCapital",
						   "participatedIn",
						   "bornIn",
						   "diedIn",
						   "happenedIn",
						   "hasFamilyName",
						   "livesIn",
						   "hasSuccessor");
	}
	
	private void checkNonRegression(List<TrecResultPredicate> results, int expectedSize, String... predicates){
		assertThat(results, hasSize(expectedSize));
		for(int i=0; i<predicates.length; i++){
			assertThat(results.get(i).value(), equalTo(predicates[i]));
		}
	}
}
