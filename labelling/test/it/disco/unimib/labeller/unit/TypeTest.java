package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.RDFResource;
import it.disco.unimib.labeller.index.Type;

import org.junit.Test;

public class TypeTest {

	@Test
	public void theDepthOfASingleNodeIs1() {
		
		Type type = new Type(new RDFResource("artist"));
		
		assertThat(type.scaledDepth(), equalTo(1.0));
	}
	
	@Test
	public void theDepthOfALeafNodeIs1() throws Exception {
		
		Type artist = new Type(new RDFResource("artist"));
		
		artist.addSuperType(new Type(new RDFResource("thing")));
		
		assertThat(artist.scaledDepth(), equalTo(1.0));
	}
}
