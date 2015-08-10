package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.TypeHierarchy;
import it.disco.unimib.labeller.properties.PropertyContextSpecificity;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class PropertyContextSpecificityTest {
	
	@Test
	public void discriminacyShouldBeGreaterIfMatchingContext() throws Exception {
		Directory directory = new RAMDirectory();
		
		EntityValues types = new EntityValues(directory).add(new TripleBuilder().withSubject("a_subject")
																				.withObject("context")
																				.asTriple())
																.closeWriter();

		EntityValues labels = new EntityValues(directory).add(new TripleBuilder().withSubject("context")
																				.withLiteral("context")
																				.asTriple())
																.closeWriter();
		new Evidence(directory,
					new TypeHierarchy(new InputFileTestDouble()),
					types,
					labels,
					new IndexFields("dbpedia"))
												.add(new TripleBuilder().withSubject("a_subject")
																		.withProperty("predicate")
																		.withLiteral("value").asTriple())
											.closeWriter();
		IndexFields fields = new IndexFields("dbpedia");
		PropertyContextSpecificity predicateAndContextWeight = new PropertyContextSpecificity(new ContextualizedEvidence(directory, new ConstantSimilarity(), fields), fields);
		
		double discriminacyMatchingContext = predicateAndContextWeight.of(new ContextualizedValues("context", new String[]{"predicate"}));
		double discriminacyNonMatchingContext = predicateAndContextWeight.of(new ContextualizedValues("non matching", new String[]{"predicate"}));
		assertThat(discriminacyMatchingContext, greaterThan(discriminacyNonMatchingContext));
	}

}
