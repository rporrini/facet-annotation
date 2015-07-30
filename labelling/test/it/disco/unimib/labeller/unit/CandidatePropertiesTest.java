package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.properties.CandidateProperties;
import it.disco.unimib.labeller.properties.PropertyDistribution;

import org.junit.Test;

public class CandidatePropertiesTest {

	@Test
	public void shouldMergeResultsForDifferentValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble()
								.resultFor("italy", "country", 10)
								.resultFor("france", "country", 25);
		
		PropertyDistribution results = new CandidateProperties(index)
									.forValues(new ContextualizedValues("any", new String[]{"italy", "france"}), 
												new OnlyValue(new IndexFields("dbpedia")));
		
		assertThat(results.scoreOf("country", "france"), equalTo(25d));
	}
}
