package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ScaledDeptComputation;

import org.junit.Test;

public class ScaledDepthComputationTest {

	@Test
	public void shouldExportAnEmptyFileWhenAnEmptyTaxonomyIsGiven() throws Exception {
		OutputFileTestDouble output = new OutputFileTestDouble();
		
		new ScaledDeptComputation().persist(new InputFileTestDouble(), output);
		
		assertThat(output.getWrittenLines(), empty());
	}
	
	@Test
	public void shouldExportTheActualComputation() throws Exception {
		OutputFileTestDouble output = new OutputFileTestDouble();
		
		new ScaledDeptComputation().persist(new InputFileTestDouble()
														.withLine(new TripleBuilder()
																			.withSubject("supertype")
																			.withObject("type")
																			.asNTriple()), 
											output);
		
		assertThat(output.getWrittenLines(), hasItem("supertype|1.0"));
		assertThat(output.getWrittenLines(), hasItem("type|0.5"));
	}
}
