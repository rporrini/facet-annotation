package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.EntityTypes;
import it.disco.unimib.labeller.index.Type;
import it.disco.unimib.labeller.index.TypeHierarchy;

import org.junit.Test;
import org.semanticweb.yars.nx.namespace.OWL;
import org.semanticweb.yars.nx.namespace.RDFS;

public class EntityTypesTest {

	@Test
	public void shouldProvideTheListOfMinimalTypesForASimpleResource() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(
										new InputFileTestDouble()
												.withLine(new TripleBuilder()
																.withSubject("thing")
																.withObject("agent")
																.asNTriple()));
		
		Type[] minimalTypes = new EntityTypes(hierarchy).minimize(new CandidateProperty("agent"));
		
		assertThat(minimalTypes[0].uri(), equalTo("agent"));
	}
	
	@Test
	public void shouldHandleTypesThatAreNotInTheHierarchy() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble());

		Type[] minimalTypes = new EntityTypes(hierarchy).minimize(new CandidateProperty("agent"));
		
		assertThat(minimalTypes[0].uri(), equalTo("agent"));
	}
	
	@Test
	public void shouldHandleMultipleTypes() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble());

		Type[] minimalTypes = new EntityTypes(hierarchy).minimize(new CandidateProperty("agent"), new CandidateProperty("actor"));
		
		assertThat(minimalTypes.length, equalTo(2));
	}
	
	@Test
	public void shouldAttachThingIfTheTypesAreUnknown() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble());

		Type[] minimalTypes = new EntityTypes(hierarchy).minimize();
		
		assertThat(minimalTypes[0].uri(), equalTo(OWL.THING.toString()));
	}
	
	@Test
	public void shouldAttachLiteralToUnknownLiterals() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble());

		Type[] minimalTypes = new EntityTypes(hierarchy).minimizeLiteral();
		
		assertThat(minimalTypes[0].uri(), equalTo(RDFS.LITERAL.toString()));
	}
	
	@Test
	public void shouldMinimizeAlsoTheDatatype() throws Exception {
		
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble());

		Type[] minimalTypes = new EntityTypes(hierarchy).minimizeLiteral(new CandidateProperty("integer"));
		
		assertThat(minimalTypes[0].uri(), equalTo("integer"));
	}
}
