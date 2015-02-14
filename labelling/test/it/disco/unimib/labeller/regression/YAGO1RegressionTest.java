package it.disco.unimib.labeller.regression;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import it.disco.unimib.labeller.benchmark.CommandLineBenchmarkSimulation;

import org.junit.Test;

public class YAGO1RegressionTest {

	@Test
	public void majority() throws Exception {
		int wordnetState108654360 = 1091252161;
		
		new CommandLineBenchmarkSimulation()
					.majority()
					.onYAGO()
					.annotate(wordnetState108654360)
					.assertThatResults(hasSize(8))
					.assertThatResults(contains(
							"hasCapital",
							"bornIn",
							"participatedIn",
							"diedIn",
							"happenedIn",
							"livesIn",
							"hasFamilyName",
							"hasSuccessor"
							));
	}
	
	@Test
	public void maximumLikelihood() throws Exception {
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
	
	@Test
	public void weightedFrequency() throws Exception {
		int wordnetState108654360 = 1091252161;
		
		new CommandLineBenchmarkSimulation()
					.weightedFrequency()
					.onYAGO()
					.annotate(wordnetState108654360)
					.assertThatResults(hasSize(8))
					.assertThatResults(contains(
							"hasCapital",
							"participatedIn",
							"bornIn",
							"diedIn",
							"happenedIn",
							"hasFamilyName",
							"livesIn",
							"hasSuccessor"
							));
	}

}
