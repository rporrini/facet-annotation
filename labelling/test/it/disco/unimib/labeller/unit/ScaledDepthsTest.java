package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.ScaledDepthComputation;
import it.disco.unimib.labeller.index.TypeHierarchy;

import org.junit.Test;

public class ScaledDepthsTest {

	@Test
	public void shouldLoadAnEmptyDepthFile() throws Exception {
		
		ScaledDepths depth = new ScaledDepths(new InputFileTestDouble());
		
		assertThat(depth.of("any"), equalTo(1.0));
	}
	
	@Test
	public void shouldGetTheRightDepth() throws Exception {
		
		ScaledDepths depth = new ScaledDepths(new InputFileTestDouble().withLine("type|0.3"));
		
		assertThat(depth.of("type"), equalTo(0.3));
	}
	
	@Test
	public void shouldParseTheOutputProducedByScaledDepthComputation() throws Exception {
		
		TypeHierarchy taxonomy = new TypeHierarchy(new InputFileTestDouble()
															.withLine(new TripleBuilder()
																	.withSubject("type")
																	.asNTriple()));
		OutputFileTestDouble output = new OutputFileTestDouble();
		new ScaledDepthComputation(taxonomy).persist(new InputFileTestDouble()
															.withLine("type"), 
													 output);
		
		ScaledDepths depth = new ScaledDepths(output.asToInputFile());
		
		assertThat(depth.of("type"), equalTo(1.0));
	}
}
