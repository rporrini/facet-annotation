package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.RDFResource;
import it.disco.unimib.labeller.index.ScaledDepth;
import it.disco.unimib.labeller.index.Type;

import org.junit.Test;
import org.semanticweb.yars.nx.Resource;

public class ScaledDepthTest {

	@Test
	public void theDepthOfASingleNodeIs1() {
		
		Type type = new Type(asResource("artist"));
		
		assertThat(new ScaledDepth().of(type), equalTo(1.0));
	}

	@Test
	public void theDepthOfALeafNodeIs1() throws Exception {
		
		Type artist = new Type(asResource("artist"));
		
		artist.addSuperType(new Type(asResource("thing")));
		
		assertThat(new ScaledDepth().of(artist), equalTo(1.0));
	}
	
	@Test
	public void theDeptOfARootNodeDependsOnHowDeepIsTheTaxonomy() throws Exception {
		
		Type thing = new Type(asResource("thing"));
		
		thing.addSubType(new Type(asResource("artist")));
		
		assertThat(new ScaledDepth().of(thing), equalTo(0.5));
	}
	
	@Test
	public void theDeptOfARootNodeDependsOnHowDeepIsTheTaxonomyAndHasEvenNumberOfNodes() throws Exception {
		
		Type thing = new Type(asResource("thing"));
		Type musician = new Type(asResource("musician"));
		
		thing.addSubType(new Type(asResource("artist")).addSubType(musician));
		
		assertThat(new ScaledDepth().of(thing), equalTo(1.0/3.0));
		assertThat(new ScaledDepth().of(musician), equalTo(1.0));
	}
	
	@Test
	public void shouldCalculateInForComplexGraphs() throws Exception {
		
		Type artist = new Type(asResource("artist"))
												.addSubType(new Type(asResource("musician")))
												.addSubType(new Type(asResource("painter"))
																.addSubType(new Type(asResource("french painter")))
																.addSubType(new Type(asResource("italian painter"))))
												.addSuperType(new Type(asResource("sensible person")))
												.addSuperType(new Type(asResource("person"))
																.addSuperType(new Type(asResource("thing")))
																.addSuperType(new Type(asResource("human"))));
		
		assertThat(new ScaledDepth().of(artist), equalTo(3.0/5.0));
	}
	
	@Test
	public void shouldStopOnLoopsOnAncestors() throws Exception {
		
		Type a = new Type(asResource("a"));
		Type b = new Type(asResource("b"));
		
		a.addSuperType(b);
		b.addSuperType(a);
		
		assertThat(new ScaledDepth().of(a), equalTo(1.0));
	}
	
	@Test
	public void shouldStopOnLoopsOnChildren() throws Exception {
		
		Type a = new Type(asResource("a"));
		Type b = new Type(asResource("b"));
		
		a.addSubType(b);
		b.addSubType(a);
		
		assertThat(new ScaledDepth().of(a), equalTo(1.0/3.0));
	}
	
	private RDFResource asResource(String uri) {
		return new RDFResource(new Resource(uri));
	}
}
