package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.TypeHierarchy;
import it.disco.unimib.labeller.properties.PropertyTypesConditionalEntropy;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class PropertyTypesConditionalEntropyTest {

	@Test
	public void entropyOfANotOccurrentPredicateShouldBeMinimal() throws Exception {
		IndexFields fields = new IndexFields("dbpedia");
		RAMDirectory directory = new RAMDirectory();
		
		EntityValues types = new EntityValues(directory).add(new TripleBuilder()
															.withSubject("CityOfParis")
															.withObject("Settlement")
															.asTriple())
		.closeWriter();
		
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), types, new EntityValues(directory).closeWriter(), fields).add(new TripleBuilder()
														  .withSubject("CityOfParis")
														  .withProperty("capitalOf")
														  .asTriple())
		.closeWriter();		
		
		PropertyTypesConditionalEntropy entropy = new PropertyTypesConditionalEntropy(new ContextualizedEvidence(directory, new ConstantSimilarity(), fields), fields);
		ContextualizedValues values = new ContextualizedValues("any", new String[]{"notExistingProperty"});
		values.setDomains("Settlement");
		
		assertThat(entropy.of(values), equalTo(0.0));
	}
	
	@Test
	public void entropyOfANotOccurrentPredicateShouldBeMoreThanZero() throws Exception {
		IndexFields fields = new IndexFields("dbpedia");
		
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
															.withSubject("CityOfParis")
															.withObject("Settlement")
															.asTriple())
														.add(new TripleBuilder()
															.withSubject("CityOfParis")
															.withObject("Place")
															.asTriple())
		.closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), types, new EntityValues(new RAMDirectory()).closeWriter(), fields)
															.add(new TripleBuilder()
																.withSubject("CityOfParis")
																.withProperty("capitalOf")
																.asTriple())
															.add(new TripleBuilder()
																.withSubject("CityOfBerlin")
																.withProperty("capitalOf")
																.asTriple())
		.closeWriter();		
		
		PropertyTypesConditionalEntropy entropy = new PropertyTypesConditionalEntropy(new ContextualizedEvidence(directory, new ConstantSimilarity(), fields), fields);
		ContextualizedValues values = new ContextualizedValues("any", new String[]{"capitalOf"});
		values.setDomains("Settlement", "Place");
		
		assertThat(entropy.of(values), equalTo(1.0));
	}
}
