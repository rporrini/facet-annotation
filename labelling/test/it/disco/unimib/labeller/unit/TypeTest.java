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
	
	@Test
	public void theDeptOfARootNodeDependsOnHowDeepIsTheTaxonomy() throws Exception {
		
		Type thing = new Type(new RDFResource("thing"));
		
		thing.addSubType(new Type(new RDFResource("artist")));
		
		assertThat(thing.scaledDepth(), equalTo(0.5));
	}
	
	@Test
	public void theDeptOfARootNodeDependsOnHowDeepIsTheTaxonomyAndHasEvenNumberOfNodes() throws Exception {
		
		Type thing = new Type(new RDFResource("thing"));
		Type musician = new Type(new RDFResource("musician"));
		
		thing.addSubType(new Type(new RDFResource("artist")).addSubType(musician));
		
		assertThat(thing.scaledDepth(), equalTo(1.0/3.0));
		assertThat(musician.scaledDepth(), equalTo(1.0));
	}
	
	@Test
	public void shouldCalculateInForComplexGraphs() throws Exception {
		
		Type artist = new Type(new RDFResource("artist"))
												.addSubType(new Type(new RDFResource("musician")))
												.addSubType(new Type(new RDFResource("painter"))
																.addSubType(new Type(new RDFResource("french painter")))
																.addSubType(new Type(new RDFResource("italian painter"))))
												.addSuperType(new Type(new RDFResource("sensible person")))
												.addSuperType(new Type(new RDFResource("person"))
																.addSuperType(new Type(new RDFResource("thing")))
																.addSuperType(new Type(new RDFResource("human"))));
		
		assertThat(artist.scaledDepth(), equalTo(3.0/5.0));
	}
	
	@Test
	public void shouldStopOnLoopsOnAncestors() throws Exception {
		
		Type a = new Type(new RDFResource("a"));
		Type b = new Type(new RDFResource("b"));
		
		a.addSuperType(b);
		b.addSuperType(a);
		
		assertThat(a.scaledDepth(), equalTo(1.0));
	}
	
	@Test
	public void shouldStopOnLoopsOnChildren() throws Exception {
		
		Type a = new Type(new RDFResource("a"));
		Type b = new Type(new RDFResource("b"));
		
		a.addSubType(b);
		b.addSubType(a);
		
		assertThat(a.scaledDepth(), equalTo(1.0/3.0));
	}
}
