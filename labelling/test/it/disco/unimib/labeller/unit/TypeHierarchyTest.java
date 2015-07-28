package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.Type;
import it.disco.unimib.labeller.index.TypeHierarchy;

import org.junit.Test;

public class TypeHierarchyTest {

	@Test
	public void onEmptyFileShouldIndexNothing() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble();
		
		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootTypes(), hasSize(1));
	}
	
	@Test
	public void shouldIndexASingleRoot() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withObject("type")
															.asNTriple());
		
		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootTypes(), hasSize(1));
	}
	
	@Test
	public void aLonelyNodeShouldHaveAFakeRoot() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withObject("type")
															.asNTriple());
		Type type = new TypeHierarchy(input).typeFrom("type");
		
		assertThat(type.superTypes().get(0).toString(), equalTo("ROOT"));
	}
	
	@Test
	public void shouldIndexARootWithManyLevelsOfInheritance() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withSubject("person")
															.withObject("agent")
															.asNTriple())
											.withLine(new TripleBuilder()
															.withSubject("politician")
															.withObject("person")
															.asNTriple());

		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootTypes(), hasSize(1));
	}
	
	@Test
	public void shouldGiveTheHierarchyAsAResults() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withSubject("person")
															.withObject("agent")
															.asNTriple())
											.withLine(new TripleBuilder()
															.withSubject("politician")
															.withObject("person")
															.asNTriple());

		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		Type root = hierarchy.typeFrom("agent");
		Type subType = root.subTypes().iterator().next();
		Type subSubType = subType.subTypes().iterator().next();
		
		assertThat(root.toString(), equalTo("agent"));
		assertThat(subType.toString(), equalTo("person"));
		assertThat(subSubType.toString(), equalTo("politician"));
	}
}
