package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ScaledDeptComputation;
import it.disco.unimib.labeller.index.TypeHierarchy;

import org.junit.Test;

public class ScaledDepthComputationTest {

	@Test
	public void shouldExportAnEmptyFileWhenAnEmptyTaxonomyIsGiven() throws Exception {
		OutputFileTestDouble output = new OutputFileTestDouble();
		
		new ScaledDeptComputation(new TypeHierarchy(new InputFileTestDouble())).persist(new InputFileTestDouble(), output);
		
		assertThat(output.getWrittenLines(), empty());
	}
	
	@Test
	public void shouldExportTheActualComputation() throws Exception {
		TypeHierarchy taxonomy = new TypeHierarchy(new InputFileTestDouble()
												.withLine(new TripleBuilder()
														.withSubject("supertype")
														.withObject("type")
														.asNTriple()));
		
		OutputFileTestDouble output = new OutputFileTestDouble();
		new ScaledDeptComputation(taxonomy).persist(new InputFileTestDouble()
													.withLine("supertype")
													.withLine("type"), 
											output);
		
		assertThat(output.getWrittenLines(), hasItem("supertype|1.0"));
		assertThat(output.getWrittenLines(), hasItem("type|0.5"));
	}
}
