package it.disco.unimib.labeller.regression;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;

public class MaximumLikelihoodRegressionTest {

	@Test
	public void dbpediaExperiments() throws Exception {
		int movieDirectors = 1888395491;
		
		new CommandLineBenchmarkSimulation()
				.maximumLikelihood()
				.onDBPedia()
				.annotate(movieDirectors)
				.assertThatResults(hasSize(27))
				.assertThatResults(contains(
						"http://dbpedia.org/property/writer",
						"http://dbpedia.org/property/director",
						"http://dbpedia.org/ontology/director",
						"http://dbpedia.org/property/producer",
						"http://dbpedia.org/ontology/writer",
						"http://dbpedia.org/ontology/producer",
						"http://dbpedia.org/property/starring",
						"http://dbpedia.org/property/editing",
						"http://dbpedia.org/ontology/starring",
						"http://dbpedia.org/ontology/editing",
						"http://dbpedia.org/property/screenplay",
						"http://dbpedia.org/property/narrator",
						"http://dbpedia.org/ontology/narrator",
						"http://dbpedia.org/property/caption",
						"http://dbpedia.org/property/studio",
						"http://dbpedia.org/property/music",
						"http://dbpedia.org/property/name",
						"http://xmlns.com/foaf/0.1/name",
						"http://dbpedia.org/property/quote",
						"http://dbpedia.org/property/title",
						"http://dbpedia.org/property/cinematography",
						"http://dbpedia.org/ontology/cinematography",
						"http://dbpedia.org/property/alt",
						"http://dbpedia.org/property/designer",
						"http://dbpedia.org/ontology/musicComposer",
						"http://dbpedia.org/property/story",
						"http://dbpedia.org/property/col"
						));
	}
	
	@Test
	public void yagoExperiments() throws Exception {
		int wordnetState108654360 = 1091252161;
		
		new CommandLineBenchmarkSimulation()
					.maximumLikelihood()
					.onYAGO()
					.annotate(wordnetState108654360)
					.assertThatResults(hasSize(8))
					.assertThatResults(contains(
							"hasCapital",
							"bornIn",
							"participatedIn",
							"diedIn",
							"happenedIn",
							"hasFamilyName",
							"hasSuccessor",
							"livesIn"
							));
	}

}
